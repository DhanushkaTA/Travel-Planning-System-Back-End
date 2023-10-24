package lk.dhanushkaTa.guideservice.repository;

import lk.dhanushkaTa.guideservice.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide,String> {
}
