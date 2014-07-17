package com.example.tutorial.plugins

import com.atlassian.sal.api.pluginsettings.PluginSettings

trait ConfigSupport {

  protected def getKey(clazz: Class[_], fieldNames: String*) =
    classOf[GlobalConfig].getName + "." + fieldNames.mkString(".")

  protected def getGlobalConfig(settings: PluginSettings, fieldName: String) =
    settings.get(getKey(classOf[GlobalConfig], fieldName)).asInstanceOf[String]

  protected def setGlobalConfig(settings: PluginSettings, fieldName: String, v: AnyRef) =
    settings.put(getKey(classOf[GlobalConfig], fieldName), v)

  protected def getProjectConfig(settings: PluginSettings, projectId: String, fieldName: String) =
    settings.get(getKey(classOf[ProjectConfig], projectId, fieldName)).asInstanceOf[String]

  protected def setProjectConfig(settings: PluginSettings, projectId: String, fieldName: String, v: AnyRef) =
    settings.put(getKey(classOf[ProjectConfig], projectId, fieldName), v)

  protected def getRoomId(settings: PluginSettings, projectId: String): String =
    getProjectConfig(settings, projectId, "roomId")

  protected def setRoomId(settings: PluginSettings, projectId: String, roomId: String): Unit =
    setProjectConfig(settings, projectId, "roomId", roomId)

  protected def getEnable(settings: PluginSettings) = getGlobalConfig(settings, "enable").toBoolean

  protected def getToken(settings: PluginSettings) = getGlobalConfig(settings, "token")

  protected def setEnable(settings: PluginSettings, enable: Boolean): Unit =
    setGlobalConfig(settings, "enable", enable.toString)

  protected def setToken(settings: PluginSettings, token: String): Unit =
    setGlobalConfig(settings, "token", token)

}
