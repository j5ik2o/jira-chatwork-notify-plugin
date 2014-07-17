package com.example.tutorial.plugins

import javax.servlet.http.HttpServletRequest
import javax.ws.rs._
import javax.ws.rs.core.{Context, MediaType, Response}

import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory
import com.atlassian.sal.api.transaction.{TransactionCallback, TransactionTemplate}
import com.atlassian.sal.api.user.UserManager
import org.slf4j.LoggerFactory

@Path("/projectConfig")
class ProjectConfigResource(private val userManager: UserManager,
                            private val pluginSettingsFactory: PluginSettingsFactory,
                            private val transactionTemplate: TransactionTemplate)
  extends ConfigSupport {

  private val LOGGER = LoggerFactory.getLogger(classOf[ProjectConfigResource])

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@PathParam("projectId") projectId: String,
          @Context request: HttpServletRequest): Response = {
    LOGGER.debug(s"get : start($request)")
    val result = transactionTemplate.execute(new TransactionCallback[ProjectConfig]() {
      override def doInTransaction() = {
        val pluginSettings = pluginSettingsFactory.createGlobalSettings()
        val projectConfig = new ProjectConfig
        val roomId = getRoomId(pluginSettings, projectId)
        projectConfig.roomId = roomId
        projectConfig
      }
    })
    LOGGER.debug(s"get : finished($request)")
    Response.ok(result).build()
  }

  @PUT
  @Consumes(Array(MediaType.APPLICATION_JSON))
  def put(@PathParam("projectId") projectId: String,
          projectConfig: ProjectConfig,
          request: HttpServletRequest): Response = {
    LOGGER.debug(s"put : start($request)")
    transactionTemplate.execute(new TransactionCallback[Unit]() {
      override def doInTransaction() = {
        val pluginSettings = pluginSettingsFactory.createGlobalSettings()
        setRoomId(pluginSettings, projectId, projectConfig.roomId)
      }
    })
    LOGGER.debug(s"put : finished($request)")
    Response.noContent().build()
  }

}
