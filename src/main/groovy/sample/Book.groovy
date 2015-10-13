package sample

import grails.persistence.Entity

@Entity
class Book {
    /* 書籍名 */
    String title

    /* 著者名 */
    String author

    // 制約
    static constraints = {
        title blank:false, maxSize: 100   // 空文字禁止、最大100文字
        author blank:false, maxSize: 100
    }
}
