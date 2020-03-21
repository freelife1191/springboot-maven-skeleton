package com.project.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by KMS on 16/10/2019.
 * S3 Mock 서버
 */
@TestConfiguration
public class S3MockConfig {

    //@Value는 Properties에서 값을 가져온다.
    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    //S3Mock을 빌드할때 포트나 메모리에 저장할 지 실제로 저장할 지 같은 것 등등을 설정 가능하다.
    @Bean("s3Mock")
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8080).withInMemoryBackend().build();
    }

    //위에서 작성한 S3Mock을 주입받는 Bean을 작성하였다.
    // 실제 테스트가 아닌 환경을 위해 작성된 Config환경과 같이 켜질 경우를 대비하여 @Primary를 넣어주었다.
    // s3Mock.start를 이용하여 Mock S3 서버를 로컬에서 시작한다.
    @Bean
    @DependsOn(value = {"s3Mock"})
    public AmazonS3Client amazonS3Client(S3Mock s3Mock){
        s3Mock.start();
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8080", region);
        AmazonS3Client client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials())) .build(); client.createBucket(bucket);
        return client;
    }
}
