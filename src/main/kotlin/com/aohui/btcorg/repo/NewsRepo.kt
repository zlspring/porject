package com.aohui.btcorg.repo

import com.aohui.btcorg.entity.NewsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NewsRepo : JpaRepository<NewsEntity, Long> {

}