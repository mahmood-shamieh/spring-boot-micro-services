package com.example.demo;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadFileEntityDto implements Serializable {
    private  long id;
    private  String fileName;
    private  String fileDownloadUri;
    private  String fileUri;
    private  String fileType;
    private  long size;
}
