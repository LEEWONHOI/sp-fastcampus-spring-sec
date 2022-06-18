package com.sp.fc.paper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "sp_paper_answer")
public class PaperAnswer {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "paperId"))
    Paper paper;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Embeddable
    public static class PaperAnswerId implements Serializable {
        private Long paperId;
        private Integer num;    // 1-base
    }

    @EmbeddedId
    private PaperAnswerId id;

    private Long problemId;
    private String answer;
    private boolean correct;

    private LocalDateTime answered; // updatable

    public Integer num() {
        return id.getNum();
    }


}
