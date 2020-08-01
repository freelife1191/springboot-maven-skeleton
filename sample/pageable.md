```
{
  "code": 100,
  "msg": "성공",
  "result": {
    // 페이징 리스트 데이터
    "content": [
      {
        "pklp_charge_type": 0,
        "pklp_charge_type_name": "전체",
        "pklp_limit_type": 1,
        "pklp_limit_type_name": "시간",
        "pklp_limit_value": 120,
        "is_over": "N",
        "pklp_vehicle_type": "A",
        "pklp_limit_group": 1,
        "pklp_del_ny": "N",
        "stor_seq": 19471,
        "stor_name": "관리사무소"
      },
      {
        "pklp_charge_type": 0,
        "pklp_charge_type_name": "전체",
        "pklp_limit_type": 1,
        "pklp_limit_type_name": "시간",
        "pklp_limit_value": 120,
        "is_over": "N",
        "pklp_vehicle_type": "A",
        "pklp_limit_group": 1,
        "pklp_del_ny": "N",
        "stor_seq": 19472,
        "stor_name": "비즈동(b1401)"
      }
    ],
    // 페이징 데이터 정보
    "pageable": {
      // sort 정보(무시)
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "offset": 0, // 다음 페이징 데이터 조회 시작 숫자 (몇번째 row부터 출력할 지. (1번째 row면 0))
      "page_number": 0, // 현재 페이지 번호 0번이 1페이지 페이지를 표현할때는 `0+1` 로 표현하면됨
      "page_size": 2, // 한 페이지의 데이터 ROW 사이즈
      "unpaged": false, // 페이징 데이터가 아니면 true
      "paged": true // 페이징 데이터 이면 true
    },
    "last": false, // true 이면 마지막 페이지 ( last=true 로 주면 마지막 페이지를 리턴 )
    "total_pages": 250, // size로 게산된 전체 페이지 수
    "total_elements": 500, // 전체 데이터 수
    "size": 2, // 한 페이지에서 보여줄 데이터 ROW 사이즈, size를 제한하지 않으면 기본값은 백엔드에서 설정한데로 지정되고 백엔드 설정도 없으면 기본값은 20
    "number": 0, // 현재 페이지 번호 0번이 1페이지 페이지를 표현할때는 `0+1` 로 표현하면됨
    // sort 정보(무시)
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "number_of_elements": 2, // 한 페이지에서 보여줄 데이터 ROW 사이즈
    "first": true, // true 이면 첫번째 페이지라는 의미 ( 요청시 first=true 로 주면 첫번째 페이지를 리턴 )
    "empty": false // 리스트 데이터가 비어있으면 true
  }
}
```