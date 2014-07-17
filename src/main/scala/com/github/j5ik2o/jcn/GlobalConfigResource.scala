package com.github.j5ik2o.jcn

import javax.servlet.http.HttpServletRequest
import javax.ws.rs._
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.{Context, MediaType, Response}

import com.atlassian.sal.api.transaction.{TransactionCallback, TransactionTemplate}
import com.atlassian.sal.api.user.UserManager
import org.slf4j.LoggerFactory

@Path("/globalConfig")
class GlobalConfigResource(private val userManager: UserManager,
                           private val globalConfigService: GlobalConfigService,
                           private val transactionTemplate: TransactionTemplate)
  extends ConfigSupport {

  private val LOGGER = LoggerFactory.getLogger(classOf[GlobalConfigResource])

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def get(@Context request: HttpServletRequest): Response = {
    LOGGER.debug(s"get : start($request)")
    val userKey = userManager.getRemoteUserKey(request)
    if (userKey != null && !userManager.isSystemAdmin(userKey)) {
      LOGGER.debug(s"get : finished($request)")
      Response.status(Status.UNAUTHORIZED).build()
    } else {
      val result = transactionTemplate.execute(new TransactionCallback[GlobalConfig]() {
        override def doInTransaction() = {
          globalConfigService.load()
        }
      })
      LOGGER.debug(s"get : finished($request)")
      Response.ok(result).build()
    }
  }

  @PUT
  @Consumes(Array(MediaType.APPLICATION_JSON))
  def put(globalConfig: GlobalConfig,
          request: HttpServletRequest): Response = {
    LOGGER.debug(s"put : start($request)")
    val userKey = userManager.getRemoteUserKey(request)
    if (userKey != null && !userManager.isSystemAdmin(userKey)) {
      LOGGER.debug(s"put : finished($request)")
      Response.status(Status.UNAUTHORIZED).build()
    } else {
      transactionTemplate.execute(new TransactionCallback[Unit]() {
        override def doInTransaction() = {
          globalConfigService.save(globalConfig)
        }
      })
      LOGGER.debug(s"put : finished($request)")
      Response.noContent().build()
    }
  }


}
