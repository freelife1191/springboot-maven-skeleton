drop table if exists common_code, common_detail_code, common_empty_code;

create table common_code
(
    id          int auto_increment comment '공통코드 ID'   primary key,
    group_code  varchar(254)                             not null comment '공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜)',
    code        int                                      not null comment '공통코드(공통코드 ID 하위의 단순증가값)',
    code_nm     varchar(254)                             not null comment '공통코드명',
    code_eng_nm varchar(254)                             not null comment '공통코드 영문명',
    code_dc     text                                     null comment '공통코드 설명',
    upper_id    int                                      null comment '상위공통코드 ID',
    depth       int default 0                   not null comment '뎁스',
    `order`     int default 0                   not null comment '정렬순서',
    path        varchar(254)                             not null comment '공통코드 ID 경로',
    path_nm     text                                     not null comment '공통코드명 경로',
    enabled     tinyint(1)   default 1                   not null comment '사용여부',
    created_id  varchar(254) default 'ADMIN'             null comment '생성자',
    created_at  datetime     default current_timestamp() null comment '생성일시',
    updated_id  varchar(254) default 'ADMIN'             null comment '수정자',
    updated_at  datetime     default current_timestamp() null comment '수정일시',
    constraint UIX_COMMON_CODE_01
        unique (upper_id, code),
    constraint UIX_COMMON_CODE_02
        unique (upper_id, code_nm),
    constraint UIX_COMMON_CODE_03
        unique (upper_id, code_eng_nm)
);

comment on table common_code is '공통 메인 코드';

create table common_detail_code
(
    common_code_id     int                                      not null comment '공통코드 ID',
    detail_code        int                                      not null comment '공통상세코드',
    detail_code_nm     varchar(254)                             not null comment '공통상세코드 명',
    detail_code_eng_nm varchar(254)                             null comment '공통상세코드 영문명',
    detail_code_dc     text                                     null comment '공통상세코드 설명',
    `order`            int default 0                            not null comment '정렬순서',
    enabled            tinyint(1)   default 1                   not null comment '사용여부',
    etc1               varchar(254)                             null comment '기타1',
    etc2               varchar(254)                             null comment '기타2',
    etc3               varchar(254)                             null comment '기타3',
    etc4               varchar(254)                             null comment '기타4',
    etc5               varchar(254)                             null comment '기타5',
    created_id         varchar(254) default 'ADMIN'             null comment '생성자',
    created_at         datetime     default current_timestamp() null comment '생성일시',
    updated_id         varchar(254) default 'ADMIN'             null comment '수정자',
    updated_at         datetime     default current_timestamp() null comment '수정일시',
    primary key (detail_code, common_code_id),
    constraint UIX_COMMON_DETAIL_CODE_01
        unique (common_code_id, detail_code),
    constraint UIX_COMMON_DETAIL_CODE_02
        unique (common_code_id, detail_code_nm)
);

comment on table common_detail_code is '공통 상세 코드';

create table common_empty_code
(
    created_at     datetime default current_timestamp() null comment '등록일시',
    group_code     varchar(254)                         not null comment '그룹 코드',
    common_code_id int                                  null comment '공통 메인 코드 ID',
    detail_code    int                                  null comment '공통 상세 코드',
    value          varchar(254)                         not null comment '공통 코드에 없는 값',
    primary key (group_code, value)
);

comment on table common_empty_code is '공통 누락 코드';