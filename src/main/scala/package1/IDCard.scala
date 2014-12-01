package package1
import java.util.Date

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.validator.constraints.NotEmpty

/**
 * Created by Dell on 9/22/2014.
 */
class IDCard {
  var card_id: String =_
  @NotEmpty
  var card_name: String =_
  @NotEmpty
  var card_number: String =_
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  @JsonFormat(pattern = "MM-dd-yyyy")
  var expiration_date: Date =_

  def getCard_id(): String = card_id
  def setCard_id(card_id: String  ) {
    this.card_id = card_id
  }
  def getCard_name(): String = card_name
  def setCard_name(card_name: String) {
    this.card_name = card_name
  }
  def getCard_number(): String = card_number
  def setCard_number(card_number: String) {
    this.card_number = card_number
  }
  def getExpiration_date(): Date = expiration_date
  def setExpiration_date(exp_date: Date) {
    this.expiration_date = exp_date
  }
}