package com.example.hackathon;

public class file {
    String filepath,filename,filesize;
    public file() {
    }

    public file(String filepath, String filename, String filesize) {
        this.filepath = filepath;
        this.filename = filename;
        this.filesize = filesize;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }
}
