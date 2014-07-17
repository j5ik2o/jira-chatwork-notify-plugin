package com.example.tutorial.plugins

import java.util.Properties

import com.atlassian.sal.api.pluginsettings.{PluginSettingsFactory, PluginSettings}
import scala.collection.JavaConverters._

case class GlobalConfigService(private val pluginSettingsFactory: PluginSettingsFactory) {

  private val globalSettings = pluginSettingsFactory.createGlobalSettings()

  protected def getKey(clazz: Class[_], fieldNames: String*) =
    clazz.getName + "." + fieldNames.mkString(".")

  //{@link String}, {@link List}, {@link Properties}, {@link Map}, or null
  protected def getGlobalConfig(pluginSettings: PluginSettings, fieldName: String): Option[Any] =
    Option(pluginSettings.get(getKey(classOf[GlobalConfig], fieldName)))

  protected def getGlobalConfigAsString(pluginSettings: PluginSettings, fieldName: String): Option[String] =
    getGlobalConfig(pluginSettings, fieldName).map(_.asInstanceOf[String])

  protected def getGlobalConfigAsProperties(pluginSettings: PluginSettings, fieldName: String): Option[Properties] =
    getGlobalConfig(pluginSettings, fieldName).map(_.asInstanceOf[Properties])

  protected def getGlobalConfigAsSeq(pluginSettings: PluginSettings, fieldName: String): Option[Seq[Any]] =
    getGlobalConfig(pluginSettings, fieldName).map(_.asInstanceOf[java.util.List[Any]].asScala)

  protected def setGlobalConfig(pluginSettings: PluginSettings, fieldName: String, v: AnyRef): Option[Any] =
    Option(pluginSettings.put(getKey(classOf[GlobalConfig], fieldName), v))

  protected def getEnable(pluginSettings: PluginSettings) = getGlobalConfigAsString(pluginSettings, "enable").map(_.toBoolean)

  protected def getToken(pluginSettings: PluginSettings) = getGlobalConfigAsString(pluginSettings, "token")

  protected def setEnable(pluginSettings: PluginSettings, enable: Boolean): Unit =
    setGlobalConfig(pluginSettings, "enable", enable.toString)

  protected def setToken(pluginSettings: PluginSettings, token: String): Unit =
    setGlobalConfig(pluginSettings, "token", token)

  def save(globalConfig: GlobalConfig): Unit = {
    setEnable(globalSettings, globalConfig.enable)
    setToken(globalSettings, globalConfig.token)
  }

  def load(): GlobalConfig = {
    val result = new GlobalConfig
    result.enable = getEnable(globalSettings).getOrElse(false)
    result.token = getToken(globalSettings).getOrElse("")
    result
  }

}
