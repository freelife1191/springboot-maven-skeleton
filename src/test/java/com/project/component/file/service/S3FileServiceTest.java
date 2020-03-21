package com.project.component.file.service;

/**
 * Created by KMS on 2019-08-21.
 * S3 Client 테스트 Private 로 변경하며 주석처리
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("dev")
public class S3FileServiceTest {
/*
    @Autowired
    S3FileService s3FileService;

    private String s3DirName = "static";
    private String fileName;
    private String s3fullPath;

    private MultipartFile multipartFile;
    private MultipartFile[] multipartFiles;

    private String fileUploadPath;
    private String fileDownloadPath;

    @Before
    public void setUp() {
        fileDownloadPath = "static/ERD_2.png";
    }
    *//**
     * 다중 파일 업로드 데이터 생성
     * @return
     *//*
    private MultipartFile[] getMultipartFileArray() {
        return new MockMultipartFile[] {
                getMultipartFile(0),
                getMultipartFile(1),
                getMultipartFile(2),
                getMultipartFile(3)
        };
    }

    private List<String> getFileList() {
        return Arrays.asList(
                "static/cross_check_yn_setting_2019_07_25_11_58_17.txt",
                "static/ERD 2019 08 21.png",
                "static/ERD.png",
                "static/ERD_2.png",
                "static/규정양식_v.0.2_20181201.pdf",
                "static/Notion-1.0.4.dmg",
                "static/jmeter.log"
        );
    }
    *//**
     * 파일 업로드 데이터 생성
     * @param index
     * @return
     *//*
    private MockMultipartFile getMultipartFile(Integer index) {
        String[] fileNameArray = new String[]{
                "cross_check_yn_setting_2019_07_25_11_58_17.txt",
                "ERD 2019 08 21.png",
                "ERD.png",
                "ERD_2.png",
                "규정양식_v.0.2_20181201.pdf",
                "Notion-1.0.4.dmg",
                "jmeter.log"
        };

        fileName = fileNameArray[index];

        s3DirName = "static";
        String localFileHome = "/Users/freelife/dev_file/";
        fileUploadPath = localFileHome+"/upload/"+fileName;
        Path path = Paths.get(fileUploadPath);
        System.out.println("# path = "+path);

        String name = fileName ;
        String originalFileName = fileName;
        String contentType = "";
//        String contentType = "image/png";
//        String contentType = "application/zip";
        byte[] content = null;

        try {
//            File file = new File(String.valueOf(path));
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    @Test
    public void 파일_업로드() {

        System.out.println("# fileUploadPath = "+fileUploadPath);

        UploadFileResponse uploadFileResponse = s3FileService.upload(getMultipartFile(0), s3DirName);


        System.out.println("### convFile = "+uploadFileResponse);
    }

    @Test
    public void 파일_다중업로드() {

        System.out.println("# fileUploadPath = "+fileUploadPath);

        List<UploadFileResponse> getFileList = s3FileService.upload(getMultipartFileArray(), s3DirName);

        System.out.println("### convFile = "+getFileList);

    }

    @Test
    public void 파일_다운로드() {
        System.out.println("# fileDownloadPath = "+fileDownloadPath);
        Resource resource = s3FileService.download(fileDownloadPath);
        System.out.println("## FileName = "+resource.getFilename());

        //#2 - 스트림을 파일로 저장함
//        String destFilename = fileDownloadPath+fileName;
//        S3ObjectInputStream inputStream = download.getObjectContent();

        *//*
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(destFilename));  //#2 - 스트림을 파일로 저장함
        } catch (IOException e) {
            e.printStackTrace();
        }
        *//*
    }

    @Test
    public void 파일_삭제() {

        fileName = "iTerm.app";
        s3fullPath = s3DirName +"/"+fileName;
        System.out.println("# s3fullPath = "+s3fullPath);

        s3FileService.delete(s3fullPath);
        s3FileService.getObjectsListFromFolder("/","static");
    }

    @Test
    public void 파일_다중_삭제() {

//        fileName = "iTerm.app";
//        s3fullPath = s3DirName +"/"+fileName;
//        System.out.println("# s3fullPath = "+s3fullPath);

        ListObjectsV2Result result = s3FileService.getObjectsListFromFolder("/", "static");
        s3FileService.delete(result);
        s3FileService.getObjectsListFromFolder("/","static");
    }

    @Test
    public void 파일_다중_삭제_List() {

        List<String> fileList = getFileList();
        s3FileService.delete(fileList);
        s3FileService.getObjectsListFromFolder("/","static");
    }

    @Test
    public void 버킷_리스트_가져오기() {
        s3FileService.getBucketList();
        //name=local-image.freelife.co.kr, creation_date=Wed Aug 14 15:39:11 KST 2019,
        //owner=d527ddb0b6f3f773b6282fd5e79d4b656423a129ab840b438b1494d4a072e404
    }


    @Test
    public void 파일_오브젝트_정보_가져오기() {
        ListObjectsV2Result result = s3FileService.getObjectsListFromFolder("/", "static");
        System.out.println("## result = "+result.getObjectSummaries());
    }

    @Test
    public void 파일_리스트_가져오기() {
        List<S3FileInfo> fileList = s3FileService.getFileList("/","static");
        fileList.forEach(file -> System.out.println("# file : "+file));
    }

    @Test
    public void 폴더_리스트_가져오기() {
        s3FileService.getFolderList(1000);
        // "" 모든 파일 리스트 가져옴
        // "/" 모든 폴더 목록 가져옴
    }

    @Test
    public void S3파일_리스트_가져오기() {
        s3FileService.getS3FileList("static", 100);
    }

    @Test
    public void 지정된_폴더_파일_리스트_가져오기() {

    }

    @Test
    public void 오브젝트_리스트_가져오기() {
        s3FileService.getObjectsList(10);
    }

    @Test
    public void 버킷_버전_리스트_가져오기() {
        s3FileService.getVersionList();
    }
    */
}