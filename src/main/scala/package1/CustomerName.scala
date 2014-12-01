package package1

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties}
import scala.reflect.{BeanProperty, BooleanBeanProperty}


@JsonIgnoreProperties(ignoreUnknown = true)
class CustomerName {
  @BeanProperty
  var customer_name: String = _

  @BeanProperty
  var message: String = _

  @BeanProperty
  var rn: String = _

  @BeanProperty
  var code: String = _
}
