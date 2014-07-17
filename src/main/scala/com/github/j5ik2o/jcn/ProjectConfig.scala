package com.github.j5ik2o.jcn

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlElement, XmlRootElement}

import scala.annotation.meta.{getter, setter}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
final class ProjectConfig {

  @XmlElement
  @(setter @getter)
  var id: String = ""

  @XmlElement
  @(setter @getter)
  var roomId: String = ""

}
