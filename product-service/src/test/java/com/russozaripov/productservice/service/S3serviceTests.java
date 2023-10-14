//package com.russozaripov.productservice.service;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectResult;
//import com.russozaripov.productservice.service.s3service.S3Service;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.BDDMockito;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@ExtendWith(MockitoExtension.class)
//public class S3serviceTests {
//
//    @Mock
//    private AmazonS3Client amazonS3Client;
//    @InjectMocks
//    private S3Service s3Service;
//
//    @DisplayName("Junit test for upload the photo operation.")
//    @Test
//    public void givenMultiPartFile_whenAddNewFile_thenReturnUrlPhoto() throws IOException {
//        byte [] mockFile = new byte[4096];
//        MockMultipartFile multipartFile = new MockMultipartFile(
//                "test-name",
//                "test-fileName.jpg",
//                "image/jpeg",
//                mockFile);
//
//        BDDMockito.given(amazonS3Client.putObject(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(InputStream.class),
//                ArgumentMatchers.any(ObjectMetadata.class)
//                )).willReturn(new PutObjectResult());
//
//        BDDMockito.willDoNothing().given(amazonS3Client).setObjectAcl(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(CannedAccessControlList.class)
//        );
//
//        String URL = "www.s3service/UrlPhoto.jpg";
//        BDDMockito.given(amazonS3Client.getResourceUrl(
//                ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyString())).willReturn(URL);
//
//
//        String photoURL = s3Service.add_New_File(multipartFile);
//
//        Assertions.assertThat(photoURL).isNotNull();
//        Assertions.assertThat(photoURL.endsWith(".jpg")).isEqualTo(true);
//        Assertions.assertThat(photoURL).isEqualTo(URL);
//    }
//}
