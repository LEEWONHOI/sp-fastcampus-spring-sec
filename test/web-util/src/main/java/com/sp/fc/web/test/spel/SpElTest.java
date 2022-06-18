package com.sp.fc.web.test.spel;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.assertj.core.api.Assertions.assertThat;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class Person {
    private String name;
    private int height;

    public boolean over(int pivot) {
        return height >= pivot;
    }
}


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class Horse {
    private String name;
    private int height;

    public boolean over(int pivot) {
        return height >= pivot;
    }
}


public class SpElTest {

    ExpressionParser parser = new SpelExpressionParser();
    Person person = Person.builder().name("홍길동").height(178).build();

    Horse nancy = Horse.builder().name("nancy").height(160).build();

    @Test
    @DisplayName("4. Context 테스트")
    public void test_4() throws Exception {

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanResolver() {
            @Override
            public Object resolve(EvaluationContext context, String beanName) throws AccessException {
                return beanName.equals("person") ? person : nancy;
            }
        });
        context.setRootObject(person);
        context.setVariable("horse", nancy);

        Boolean result1 = parser.parseExpression("@person.over(170)").getValue(context, Boolean.class);
        assertThat(result1).isTrue();

        Boolean result2 = parser.parseExpression("@nancy.over(170)").getValue(context, Boolean.class);
        assertThat(result2).isFalse();

        Boolean result3 = parser.parseExpression("over(170)").getValue(context, Boolean.class);
        assertThat(result3).isTrue();

        Boolean result4 = parser.parseExpression("#horse.over(170)").getValue(context, Boolean.class);
        assertThat(result4).isFalse();

    }


    @Test
    @DisplayName("3. 메소드 호출")
    public void test3() throws Exception {
        Boolean result1 = parser.parseExpression("over(170)").getValue(person, Boolean.class);
        assertThat(result1).isTrue();

        Boolean result2 = parser.parseExpression("over(170)").getValue(nancy, Boolean.class);
        assertThat(result2).isFalse();
    }


    @Test
    @DisplayName("2. 값 변경")
    public void test2() throws Exception {
        parser.parseExpression("name").setValue(person, "호나우드");
        assertThat("호나우드")
                .isEqualTo(parser.parseExpression("name").getValue(person, String.class));
    }

    @Test
    @DisplayName("1. 기본테스트")
    public void test1() throws Exception {
        assertThat("홍길동").isEqualTo(parser.parseExpression("name").getValue(person));
    }

}

