package package1

/**
 * Created by Dell on 9/23/2014.
 */
import org.hibernate.validator.constraints.NotEmpty

class WebLogin {
  var login_id : String = _
  @NotEmpty
  var url : String = _
  @NotEmpty
  var login : String = _
  @NotEmpty
  var password : String = _

  def getLogin_id :String = login_id
  def setLogin_id(login_id : String)
  {
    this.login_id = login_id
  }
  def getUrl : String = url
  def setUrl(url: String): Unit =
  {
    this.url = url
  }
  def getLogin : String = login
  def setLogin(login : String): Unit =
  {
    this.login = login
  }
  def getPassword : String = password
  def setPassword(password : String): Unit =
  {
    this.password = password
  }
}