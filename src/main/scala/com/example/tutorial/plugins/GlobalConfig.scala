package com.example.tutorial.plugins

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlElement, XmlRootElement}

import scala.annotation.meta.{setter, getter}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
final class GlobalConfig {

  @(getter @setter)
  @XmlElement
  var enable: Boolean = false

  @(getter @setter)
  @XmlElement
  var token: String = ""

}
