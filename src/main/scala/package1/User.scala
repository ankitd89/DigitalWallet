package package1

import java.util.Date
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {
  @Id
  var user_id: String = _
  @NotEmpty
  var email: String = _
  @NotEmpty
  var password: String = _
  //@JsonIgnore
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  var name: String = _
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  var created_time: Date = _
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  var updated_time: Date = _

  var listIdCard = new java.util.ArrayList[IDCard]
  var listWebLogin = new java.util.ArrayList[WebLogin]
  var listBankAccount = new java.util.ArrayList[BankAccount]

  def setCreated_time(created_time: Date) {
    this.created_time = created_time
  }
  def setUpdated_time(updated_time: Date) {
    this.updated_time = updated_time
  }
  def getUpdated_time(): Date = updated_time
  def getCreated_time(): Date = created_time
  def getUser_id(): String = user_id
  def setUser_id(user_id: String) {
    this.user_id = user_id
  }
  def getEmail(): String = email
  def setEmail(email: String) {
    this.email = email
  }
  def getPassword(): String = password
  def setPassword(password: String) {
    this.password = password
  }
  def getName(): String = name
  def setName(name: String) {
    this.name = name
  }
}

