package com.example.tmall.util;
import java.io.Serializable;
import java.util.List;
//将基于 JPA 的分页结果转换为带有分页导航功能的 Page4Navigator 对象，方便在前端或其他需要分页导航的场景中使用。
//它封装了分页信息和导航页码的计算，提供了便捷的属性和方法来访问和操作分页数据。
import org.springframework.data.domain.Page;

/**
 * 作者：cjy
 * 类名：Page4Navigator
 * 全路径类名：com.example.tmall.util.Page4Navigator
 * 父类或接口：@see Serializable
 * 描述：
 * 作者：cjy
 * 类名：Page4Navigator
 * 全路径类名：com.example.tmall.util.Page4Navigator
 * 父类或接口：@see Serializable
 * 描述：page4导航器
 */
public class Page4Navigator<T> implements Serializable {
    Page<T> pageFromJPA;
    int navigatePages; // 导航页码数量
    int totalPages; // 总页数
    int number; // 当前页码
    long totalElements; // 总元素数量
    int size; // 每页元素数量
    int numberOfElements; // 当前页元素数量
    List<T> content; // 当前页元素列表
    boolean isHasContent; // 是否有内容
    boolean first; // 是否为第一页
    boolean last; // 是否为最后一页
    boolean isHasNext; // 是否有下一
    boolean isHasPrevious; // 是否有上一页
    int[] navigatepageNums; // 导航页码数组


    public Page4Navigator() {
        // 这个空的分页是为了 Redis 从 json 格式转换为 Page4Navigator 对象而专门提供的
    }

    public Page4Navigator(Page<T> pageFromJPA, int navigatePages) {
        this.pageFromJPA = pageFromJPA;
        this.navigatePages = navigatePages;

        totalPages = pageFromJPA.getTotalPages();

        number = pageFromJPA.getNumber();

        totalElements = pageFromJPA.getTotalElements();

        size = pageFromJPA.getSize();

        numberOfElements = pageFromJPA.getNumberOfElements();

        content = pageFromJPA.getContent();

        isHasContent = pageFromJPA.hasContent();

        first = pageFromJPA.isFirst();

        last = pageFromJPA.isLast();

        isHasNext = pageFromJPA.hasNext();

        isHasPrevious = pageFromJPA.hasPrevious();

        calcNavigatepageNums();
    }

    private void calcNavigatepageNums() {
        int navigatepageNums[];
        int totalPages = getTotalPages();
        int num = getNumber();
        // 当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { // 当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                // 最前 navigatePages 页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                // 最后 navigatePages 页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                // 所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
        this.navigatepageNums = navigatepageNums;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isHasContent() {
        return isHasContent;
    }

    public void setHasContent(boolean isHasContent) {
        this.isHasContent = isHasContent;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasNext() {
        return isHasNext;
    }

    public void setHasNext(boolean isHasNext) {
        this.isHasNext = isHasNext;
    }

    public boolean isHasPrevious() {
        return isHasPrevious;
    }

    public void setHasPrevious(boolean isHasPrevious) {
        this.isHasPrevious = isHasPrevious;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }


}
