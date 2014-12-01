package package1

import java.util.Calendar
import java.util.concurrent.atomic.AtomicLong
import javax.validation.Valid
import javax.ws.rs.core.EntityTag
import javax.ws.rs.core
import org.bson.types.ObjectId
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.{Update, Criteria, Query}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.{MediaType, HttpHeaders, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation._
import org.springframework.web.client.RestTemplate
import package1.RequestController._
import java.util.LinkedList
import scala.util.control._
import org.springframework.context.ApplicationContext

object RequestController {
}

@RestController
class RequestController {
  val ctx = new AnnotationConfigApplicationContext(classOf[SpringMongoConfig])
  val mongoOperation = ctx.getBean("mongoTemplate").asInstanceOf[MongoOperations]
  val calendar = Calendar.getInstance

  //Create user
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = Array("/api/v1/users"), method = Array(RequestMethod.POST), consumes = Array("application/json"))
  def user(@Valid @RequestBody(required = true) user: User): User = {
    user.setCreated_time(calendar.getTime)
    user.setUpdated_time(calendar.getTime)
    user.setUser_id("u-" + new ObjectId())
    mongoOperation.save(user)
    user
  }
  //Get user
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = Array("/api/v1/users/{user_id}"),method = Array(RequestMethod.GET))
  def getUser(@PathVariable("user_id") user_id: String, @RequestHeader(value="Etag", required=true , defaultValue="") eTag: String): Any  = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val savedUser = mongoOperation.findOne(searchUserQuery, classOf[User])
    val tag: String = eTag
    val cacheControl: core.CacheControl = new core.CacheControl
    cacheControl.setMaxAge(500)
    val etag : EntityTag = new EntityTag(Integer.toString(savedUser.updated_time.hashCode()))
    val httpResponseHeader: HttpHeaders = new HttpHeaders()
    httpResponseHeader.setCacheControl(cacheControl.toString)
    httpResponseHeader.add("Etag", etag.getValue)
    if(etag.getValue.equalsIgnoreCase(tag)){
      new ResponseEntity[String]( null, httpResponseHeader, HttpStatus.NOT_MODIFIED )
    }
    else {
      new ResponseEntity[User]( savedUser, httpResponseHeader, HttpStatus.OK )
    }
  }

  //Update user
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = Array("/api/v1/users/{user_id}"),method = Array(RequestMethod.PUT), consumes = Array("application/json"))
  def updateUser(@PathVariable("user_id") user_id: String, @Valid @RequestBody(required = true) user: User): User = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    var update = new Update()
    update.set("email", user.email);
    update.set("password", user.password);
    if(user.name != null)
      update.set("name", user.name);
    val c = Calendar.getInstance()
    update.set("updated_time", c.getTime);
    mongoOperation.updateFirst(searchUserQuery, update, classOf[User])
    val updatedUser = mongoOperation.findOne(searchUserQuery, classOf[User])
    updatedUser
  }

  //Add card
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/idcards"),method = Array(RequestMethod.POST), consumes = Array("application/json"))
  def cardId(@PathVariable("user_id") user_id: String, @Valid @RequestBody(required = true) cardId: IDCard): IDCard = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    cardId.setCard_id("c-"+new ObjectId())
    u.listIdCard.add(cardId)
    mongoOperation.save(u)
    cardId
  }
  //  Display all Id Cards
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/idcards"),method = Array(RequestMethod.GET), consumes = Array("application/json"))
  def getAllCardId(@PathVariable("user_id") user_id: String): java.util.ArrayList[IDCard] = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    u.listIdCard
  }
  // Delete ID Card
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/idcards/{card_id}"), method = Array(RequestMethod.DELETE), consumes = Array("application/json"))
  def deleteCard(@PathVariable("user_id") user_id: String, @PathVariable("card_id") card_id: String) = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    var increment = 0
    val listSize = u.listIdCard.size()
    val loop = new Breaks
    loop.breakable{
      while(increment<listSize) {
        val idCard = u.listIdCard.get(increment)
        if (idCard.getCard_id().equals(card_id)) {
          u.listIdCard.remove(increment)
          loop.break
        }
        increment = increment+1
      }
    }
    mongoOperation.save(u)
  }

  //Add Web Login
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/weblogins"),method = Array(RequestMethod.POST), consumes = Array("application/json"))
  def addWebLogin(@PathVariable("user_id") user_id: String, @Valid @RequestBody(required = true) webLogin : WebLogin): WebLogin = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    webLogin.setLogin_id("l-"+new ObjectId())
    u.listWebLogin.add(webLogin)
    mongoOperation.save(u)
    webLogin
  }
  //  Display all web login
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/weblogins"),method = Array(RequestMethod.GET), consumes = Array("application/json"))
  def getAllWebLogin(@PathVariable("user_id") user_id: String): java.util.ArrayList[WebLogin] = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    u.listWebLogin
  }
  // Delete Web Login
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/weblogins/{login_id}"), method = Array(RequestMethod.DELETE), consumes = Array("application/json"))
  def deleteWebLogin(@PathVariable("user_id") user_id: String, @PathVariable("login_id") login_id: String) = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    var increment = 0
    val listSize = u.listWebLogin.size()
    val loop = new Breaks
    loop.breakable{
      while(increment<listSize) {
        val webLogin = u.listWebLogin.get(increment)
        if (webLogin.getLogin_id.equals(login_id)) {
          u.listWebLogin.remove(increment)
          loop.break
        }
        increment = increment+1
      }
    }
    mongoOperation.save(u)
  }

  //Add Bank Account
  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/bankaccounts"),method = Array(RequestMethod.POST), consumes = Array("application/json"))
  def addBankAccount(@PathVariable("user_id") user_id: String, @Valid @RequestBody(required = true) bankAccount : BankAccount): Any = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    bankAccount.setBa_id("b-"+new ObjectId())
    val rn = bankAccount.getRouting_number
    val restTemplate = new RestTemplate()

    ///////////////Adding Convertor for Text/Plain output////////////////////
    val converter = new MappingJackson2HttpMessageConverter
    val supportedMediaTypes = new LinkedList[MediaType](converter.getSupportedMediaTypes)
    val textJavascriptMediaType = new MediaType("text", "plain", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)
    supportedMediaTypes.add(textJavascriptMediaType)
    converter.setSupportedMediaTypes(supportedMediaTypes)
    restTemplate.getMessageConverters.add(converter)
    val page = restTemplate.getForObject("https://routingnumbers.herokuapp.com/api/data.json?rn="+rn, classOf[CustomerName])
    val code = page.getCode
    if(code.equalsIgnoreCase("200")) {
      bankAccount.setAccount_name(page.getCustomer_name)
      u.listBankAccount.add(bankAccount)
      mongoOperation.save(u)
      bankAccount
    }
    else
     page

  }

  //  Display all Bank Accounts
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/bankaccounts"),method = Array(RequestMethod.GET), consumes = Array("application/json"))
  def getAllBankAccount(@PathVariable("user_id") user_id: String): java.util.ArrayList[BankAccount] = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    u.listBankAccount
  }
  // Delete Bank Account
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = Array("/api/v1/users/{user_id}/bankaccounts/{ba_id}"), method = Array(RequestMethod.DELETE), consumes = Array("application/json"))
  def deleteBankAccount(@PathVariable("user_id") user_id: String, @PathVariable("ba_id") ba_id: String) = {
    val searchUserQuery = new Query(Criteria.where("_id").is(user_id))
    val u = mongoOperation.findOne(searchUserQuery, classOf[User])
    var increment = 0
    val listSize = u.listBankAccount.size()
    val loop = new Breaks
    loop.breakable{
      while(increment<listSize) {
        val bankAccount = u.listBankAccount.get(increment)
        if (bankAccount.getBa_id.equals(ba_id)) {
          u.listBankAccount.remove(increment)
          loop.break
        }
        increment = increment+1
      }
    }
    mongoOperation.save(u)
  }
}
