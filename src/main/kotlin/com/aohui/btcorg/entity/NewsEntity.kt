package com.aohui.btcorg.entity

import javax.persistence.*

@Entity
data class NewsEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "news_id")
        @SequenceGenerator(name = "news_id", sequenceName = "NEWS_ID_SEQ")
        var id: Long? = null

)