package org.example.springassignment2.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.example.springassignment2.entity.User;
import org.example.springassignment2.entity.Post;
import org.example.springassignment2.entity.Comment;

import java.time.LocalDateTime;

@Profile("local") // local 환경에서만 실행되도록 설정
@Component
@RequiredArgsConstructor
public class InitUser {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        initDataService.init();
    }

    @Component
    static class InitDataService {

        /**
         * @PersistenceContext 사용이유
         * EntityManager를 빈으로 주입할 때 사용하는 어노테이션입니다.
         * EntityManager를 사용할 때 주의해야 할 점은
         * 여러 쓰레드가 동시에 접근하면 동시성 문제가 발생하여 쓰레드 간에는 EntityManager를 공유해서는 안됩니다.
         */
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            // 기존 데이터 삭제
            em.createQuery("DELETE FROM Comment").executeUpdate();
            em.createQuery("DELETE FROM Post").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            // 초기 사용자 및 글 데이터 등록
            for (int i = 1; i <= 5; i++) {
                User user = new User("kim" + i, "kim" + i + "@example.com");
                em.persist(user);

                // 글 데이터 생성
                for (int j = 1; j <= 2; j++) {
                    Post post = new Post("Title " + j + " by " + user.getName(),
                            "Body content for post " + j,
                            user);
                    em.persist(post);
                }
            }
            System.out.println("초기 데이터 등록 완료!");
        }
    }
}
