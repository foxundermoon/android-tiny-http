package com.getui.sdk.util.http_tiny;

import android.text.TextUtils;


import com.getui.sdk.util.http_tiny.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by fox on 9/21/16.
 */

public class DefaultHttpRequest extends HttpRequest {

    @Override
    public HttpResponse execute() throws IllegalUsageException, HttpException {
        checkIllegalUsage();
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.getName());
            if (connectTimeout > 0) {
                connection.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                connection.setReadTimeout(readTimeout);
            }

            if (!isHeadersEmpty()) {
                for (Map.Entry<String, Object> entry :
                        headers.entrySet()
                        ) {
                    connection.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            if (isMultipartsEmpty() && body == null) {
                connection.setDoOutput(false);
            } else {
                connection.setDoOutput(true);
            }
            connection.setDoInput(true);
            connection.setUseCaches(isUseCache);
            if (body != null) {
                writeBodyToConnection(connection);
            } else if (!isMultipartsEmpty()) {
                writeMultipartToConnection(connection);
            }

            int status = connection.getResponseCode();

            HttpHeaders headers = getHttpHeadersFromConnections(connection);

            ByteArrayOutputStream byteArrayOutputStream = null;
            if (isHandleResponseBody) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[64];
                int len = -1;
                inputStream = connection.getInputStream();
                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
            }

            return new HttpResponse(status, byteArrayOutputStream == null ? null : byteArrayOutputStream.toByteArray(), headers);


        } catch (IOException e) {
            throw new HttpException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            IOUtils.closeQuietly(inputStream);
        }
    }

    private HttpHeaders getHttpHeadersFromConnections(HttpURLConnection connection) {
        Map<String, List<String>> headerFields = connection.getHeaderFields();

        HttpHeaders headers = new HttpHeaders();
        if (headerFields != null) {
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if (entry.getValue() != null && entry.getValue().size() != 0) {
                    if (!TextUtils.isEmpty(entry.getKey())) {
                        for (int i = 0; i < entry.getValue().size(); i++) {
                            if (i == 0) {
                                headers.put(entry.getKey(), entry.getValue().get(i));
                            } else {
                                headers.put(entry.getKey() + "." + i, entry.getValue().get(i));
                            }
                        }
                    }

                }
            }
        }
        return headers;
    }

    private void writeMultipartToConnection(HttpURLConnection connection) throws IOException, IllegalUsageException {
        HttpMultipart multipart = new HttpMultipart();
        for (Part p :
                multiparts) {
            Object content = p.getContent();
            switch (p.getPartType()) {
                case String:
                    multipart.addFormField(p.getName(), String.valueOf(p.getContent()));
                    break;
                case File:
                    File file = (File) p.getContent();
                    multipart.addFormPart(p.getName(), file, p.getFilename(), p.getMediaType());
                    break;
                default:
                    throw new IllegalUsageException("the part type is not support yet! :" + p.getContent().getClass().getCanonicalName());
//                case Bytes:
//                    multipart.addFormPart();
            }
        }
        multipart.config(connection);
        multipart.writeFlush(connection);

    }

    private void writeBodyToConnection(HttpURLConnection connection) throws HttpException, IllegalUsageException {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = connection.getOutputStream();
            switch (body.getType()) {
                case File:
                    inputStream = new FileInputStream(body.getFileBody());
                    break;
                case Bytes:
//                    inputStream = new ByteArrayInputStream(body.getBytesBody());
                    byte[] bytesBody = body.getBytesBody();
                    outputStream.write(bytesBody, 0, bytesBody.length);
                    outputStream.flush();
                    return;
                case Stream:
                    inputStream = body.getInputStreamBody();
                    break;
                default:
                    throw new IllegalUsageException("not supported body type");
            }
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
            throw new HttpException(e);
        } catch (Exception e) {
            throw new HttpException(e);
        }
    }


}
