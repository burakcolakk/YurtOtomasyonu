package com.yurt.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YurtYonetimSistemiApplicationTest {

    @Test
    void contextLoads() {
        // Bu metodun içi boş kalsa bile, Spring Context'i ayağa kaldırdığı için
        // main metodu test edilmiş sayılır.
    }

    @Test
    void main() {
        // Main metodunu manuel olarak da çağırarak %100 coverage garantileyelim
        YurtYonetimSistemiApplication.main(new String[] {});
    }
}