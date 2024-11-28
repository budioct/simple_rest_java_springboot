package budhioct.dev.utilities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class RestResponse {

    @Getter
    @Setter
    @Data
    @Builder
    public static class list<T>{
        private Integer status_code;
        private String message;
        private String errors;
        private restPagingResponse paging;
        private T list;
    }

    @Getter
    @Setter
    @Data
    @Builder
    public static class object<T> {
        private T data;
        private Integer status_code;
        private String message;
        private String errors;
    }

    @Getter
    @Setter
    @Data
    @Builder
    public static class restError<T> {
        private Integer status_code;
        private String message;
        private String errors;
    }

    @Getter
    @Setter
    @Data
    @Builder
    public static class restPagingResponse {
        private Integer currentPage;
        private Integer totalPage;
        private Integer sizePage;

    }

}
