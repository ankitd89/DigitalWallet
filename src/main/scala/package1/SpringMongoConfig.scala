package package1

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import com.mongodb.{MongoClientURI, MongoClient}

@Configuration
class SpringMongoConfig {

  @Bean
  def mongoDbFactory(): MongoDbFactory = {
   new SimpleMongoDbFactory(new MongoClient(new MongoClientURI("mongodb://ankitd89:Assignment2@ds047930.mongolab.com:47930/assign2")), "assign2")
  }

  @Bean
  def mongoTemplate(): MongoTemplate = {
    val mongoTemplate = new MongoTemplate(mongoDbFactory())
    mongoTemplate
  }
}