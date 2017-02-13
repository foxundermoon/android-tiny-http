package com.getui.sdk.util.http_tiny;

import com.getui.sdk.util.http_tiny.utils.IOUtils;
import com.getui.sdk.util.http_tiny.utils.StringUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class HttpMultipart {

    private static final String BOUNDARY_FIX = "========";

    private static final String LINE_FEED = "\r\n";

    private static final int MAX_BUFFER_SIZE = 8192;

    private final String boundary;

    private final List<FormField> fields;

    private final List<FormPart> files;

    private int contentLength = 0;

    public HttpMultipart() {
        this.boundary = BOUNDARY_FIX + StringUtils.random() + BOUNDARY_FIX;
        this.fields = new ArrayList<FormField>();
        this.files = new ArrayList<FormPart>();
    }

    public void config(HttpURLConnection connection) {
        int length = getContentLength();
        connection.setFixedLengthStreamingMode(length);

        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Content-Length", String.valueOf(length));
    }

    public void addFormField(String name, int value) {
        addFormField(name, String.valueOf(value));
    }

    public void addFormField(String name, String value) {
        String content = fieldContent(name, value);
        contentLength += StringUtils.toBytes(content).length;
        fields.add(new FormField(name, value));
    }

    public void addFormPart(String name, String filePath, String fileName, String contentType) throws IOException {
        addFormPart(name, new File(filePath), fileName, contentType);
    }

    public void addFormPart(String name, File file, String fileName, String contentType) throws IOException {
        String start = partContentStart(name, fileName, contentType);
        String end = partContentEnd();
        String content = start + end;
        contentLength += StringUtils.toBytes(content).length;
        contentLength += file.length();

        if (StringUtils.isEmpty(fileName)) {
            files.add(new FormPart(name, file, file.getName(), contentType));
        } else {
            files.add(new FormPart(name, file, fileName, contentType));
        }
    }

    public int getContentLength() {
        String end = multipartEnd();
        return contentLength + StringUtils.toBytes(end).length;
    }

    public String getBoundary() {
        return boundary;
    }

    private String fieldContent(final String name, final String value) {
        String content = "--" + boundary + LINE_FEED;
        content += "Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED;
        content += "Content-Type: text/plain; charset=UTF-8" + LINE_FEED;
        content += LINE_FEED;
        content += value;
        content += LINE_FEED;
        return content;
    }

    private String partContentStart(final String name, final String fileName, final String contentType) throws IOException {
        String content = "--" + boundary + LINE_FEED;
        content += "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"" + LINE_FEED;
        if (!StringUtils.isEmpty(contentType)) {
            content += "Content-Type: " + contentType + LINE_FEED;
        }
        content += LINE_FEED;
        return content;
    }

    private String partContentEnd() {
        return LINE_FEED;
    }

    private String multipartEnd() {
        return "--" + boundary + "--" + LINE_FEED;
    }

    public void writeFlush(HttpURLConnection connection) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public void write(OutputStream outputStream) throws IOException {
        for (FormField field : fields) {
            String content = fieldContent(field.getName(), field.getValue());
            outputStream.write(StringUtils.toBytes(content));
        }

        for (FormPart part : files) {
            String partStart = partContentStart(part.getName(), part.getFileName(), part.getContentType());
            String partEnd = partContentEnd();

            // Write file part header
            outputStream.write(StringUtils.toBytes(partStart));

            // Write file content
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(part.getFile());
                int bufferSize = Math.min(inputStream.available(), MAX_BUFFER_SIZE);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

            // Write file part footer
            outputStream.write(StringUtils.toBytes(partEnd));
        }

        String end = multipartEnd();
        outputStream.write(StringUtils.toBytes(end));
    }

    private static class FormField {
        private String name;
        private String value;

        public FormField(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class FormPart {
        private String name;
        private File file;
        private String fileName;
        private String contentType;

        public FormPart(String name, String filePath, String fileName, String contentType) {
            this.name = name;
            this.file = new File(filePath);
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public FormPart(String name, File file, String fileName, String contentType) {
            this.name = name;
            this.file = file;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName() {
            if (StringUtils.isEmpty(fileName)) {
                return file.getName();
            }
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }
}
