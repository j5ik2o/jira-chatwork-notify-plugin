package com.github.j5ik2o.jcn

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlElement, XmlRootElement}

import scala.annotation.meta.{getter, setter}

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
