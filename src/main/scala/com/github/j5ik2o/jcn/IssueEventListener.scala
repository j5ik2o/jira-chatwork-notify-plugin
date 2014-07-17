package com.github.j5ik2o.jcn

import java.util

import com.atlassian.event.api.{EventListener, EventPublisher}
import com.atlassian.jira.event.`type`.EventType
import com.atlassian.jira.event.issue.IssueEvent
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.{Consts, HttpResponse, NameValuePair}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.{DisposableBean, InitializingBean}

class IssueEventListener(private val eventPublisher: EventPublisher,
                         private val pluginSettingFactory: PluginSettingsFactory)
  extends InitializingBean with DisposableBean with ConfigSupport {

  private val settings = pluginSettingFactory.createGlobalSettings()

  private val log = LoggerFactory.getLogger(classOf[IssueEventListener])

  private val httpClient = HttpClients.createDefault()

  private def url(roomId: String): String =
    s"https://api.chatwork.com/v1/rooms/${roomId}/messages"

  private def sendMessageRequest(projectId: String, message: String): Option[HttpResponse] = {
    if (getEnable(settings)) {
      val roomId = getRoomId(settings, projectId)
      val request = new HttpPost(url(roomId))
      request.addHeader("X-ChatWorkToken", getToken(settings))
      val params = new util.ArrayList[NameValuePair]()
      params.add(new BasicNameValuePair("body", message))
      val entity = new UrlEncodedFormEntity(params, Consts.UTF_8)
      request.setEntity(entity)
      Some(httpClient.execute(request))
    } else {
      None
    }
  }

  override def afterPropertiesSet(): Unit = {
    eventPublisher.register(this)
  }

  override def destroy(): Unit = {
    eventPublisher.unregister(this)
  }

  @EventListener
  def onIssueEvent
  (issueEvent: IssueEvent): Unit = {
    val issue = issueEvent.getIssue
    val projectId = issue.getProjectObject.getId.toString
    val eventTypeId = issueEvent.getEventTypeId

    if (eventTypeId == EventType.ISSUE_CREATED_ID) {
      sendMessageRequest(projectId, s"Issue ${issue.getKey} has been created at ${issue.getCreated}.")
      log.info("Issue {} has been created at {}.", issue.getKey, issue.getCreated)
    } else if (eventTypeId == EventType.ISSUE_RESOLVED_ID) {
      sendMessageRequest(projectId, s"Issue ${issue.getKey} has been resolved at ${issue.getResolutionDate}.")
      log.info("Issue {} has been resolved at {}.", issue.getKey, issue.getResolutionDate)
    } else if (eventTypeId == EventType.ISSUE_CLOSED_ID) {
      sendMessageRequest(projectId, s"Issue ${issue.getKey} has been closed at ${issue.getUpdated}.")
      log.info("Issue {} has been closed at {}.", issue.getKey, issue.getUpdated)
    }

  }
}
