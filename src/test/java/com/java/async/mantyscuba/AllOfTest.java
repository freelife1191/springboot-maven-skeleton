package com.java.async.mantyscuba;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * CompletableFuture 사용패턴별 활용 예
 *
 * 동시에 n개의 요청을 호출하고 모든 호출이 완성되면 진행하기
 *
 * 동시에 3개의 Rest 요청을 보내고 3개의 요청이 완료되면 Callback을 진행하기 위한 예제
 * 아래 예는 5초 뒤에 "Completed!!" 를 반환하는 buildMessage 라는 메서드를 동시에 3개를 호출하고 3개가 완료되었을때 그 결과를 마지막 thenAcceptAsync 에서 모두 모아서 처리를 한다
 * 선후 관계가 없는 데이터를 동시에 조회 할때 적절히 사용할 수 있다
 * Created by KMS on 16/04/2020.
 */
@Slf4j
public class AllOfTest {

    int i =0;
    private String buildMessage() {
        try {
            Thread.sleep(5 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Completed!!"+ (i++);
    }

    @Test
    public void allofTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(this::buildMessage);

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture[3]))
                .thenApplyAsync(result -> completableFutures.stream().map(future -> future.join()).collect(Collectors.toList()))
                .thenAcceptAsync(messages -> messages.forEach(message -> System.out.println(message)));

        Thread.sleep(11 * 1000L);
    }

    private List<String> dataTest() {
        try {
            Thread.sleep(5 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList("a","b","c");
    }

    private List<String> dataSubTest(List<String> paramList) {
        try {
            Thread.sleep(3 * 1000L);
            System.out.println("## paramList = "+paramList);
            System.out.println("## dataSubTest = "+dataList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList("a","b","c");
    }

    private List<String> dataTest2() {
        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList("e","f","g");
    }

    private List<String> dataTest3() {
        try {
            Thread.sleep(10 * 1000L);
            dataList3 = Arrays.asList("h","i","j");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataList3;
    }

    List<String> dataList;
    List<String> dataList2;
    List<String> dataList3;

    @Test
    public void allofTest2() throws Exception {
        CompletableFuture<List<String>> cf1 = CompletableFuture.supplyAsync(this::dataTest);
        CompletableFuture<List<String>> cf2 = CompletableFuture.supplyAsync(this::dataTest2);
        CompletableFuture<List<String>> cf3 = CompletableFuture.supplyAsync(this::dataTest3);

        CompletableFuture<Void> dataFuture = cf1
                .thenApply(data -> {
                    dataList = data;
                    return data;
                })
                .thenApply(data -> dataSubTest(data))
                .thenAccept(data -> System.out.println("end1 = "+data));

        CompletableFuture<Void> dataFuture2 = cf2
                .thenApply(data -> {
                    dataList2 = data;
                    return data;
                })
                .thenAccept(data -> System.out.println("end2 = "+data));

        CompletableFuture<Void> dataFuture3 = cf3
                .thenAccept(data -> System.out.println("end3 = "+dataList3));

        CompletableFuture
                .allOf(dataFuture, dataFuture2, dataFuture3)
                .join();
        System.out.println("Complete!!");
    }

    private CompletableFuture<String> downloadWebPage(String pageLink) {
        return CompletableFuture.supplyAsync(() -> {
            // Code to download and return the web page's content
            return pageLink+"_content";
        });
    }

    @Test
    public void completableFutureAllOfTest() {
        List<String> webPageLinks = Arrays.asList("http://www.naver.com", "http://toast.com", "http://godo.com"); // A list of 100 web page links
        // Download contents of all the web pages asynchronously
        List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
                .map(webPageLink -> downloadWebPage(webPageLink))
                .collect(Collectors.toList());

        // Create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()])
        );

        // When all the Futures are completed, call `future.join()` to get their results and collect the results in a list -
        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> {
            return pageContentFutures.stream()
                    .map(pageContentFuture -> pageContentFuture.join())
                    .collect(Collectors.toList());
        });

        // Count the number of web pages having the "CompletableFuture" keyword.
        CompletableFuture<Long> countFuture = allPageContentsFuture.thenApply(pageContents -> {
            return pageContents.stream()
                    .filter(pageContent -> pageContent.contains("CompletableFuture"))
                    .count();
        });

        try {
            System.out.println("Number of Web Pages having CompletableFuture keyword - " + countFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
