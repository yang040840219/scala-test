package com.metrics

import java.net.InetSocketAddress

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.servlets._
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.slf4j.LoggerFactory

/**
 * Created by yxl on 15/10/31.
 */
class HttpMetricsServer(bindAddress: String, port: Int, metrics: MetricRegistry) {

  val logger = LoggerFactory.getLogger(this.getClass)

  private var server: Server = null

  def init(): Unit = {
    // creating the socket address for binding to the specified address and port
    val inetSocketAddress = new InetSocketAddress(bindAddress, port);

    // create new Jetty server
    server = new Server(inetSocketAddress);

    // creating the servlet context handler
    val servletContextHandler = new ServletContextHandler();

    // setting the context path
    servletContextHandler.setContextPath("/");

    // adding the codahale metrics servlet to the servlet context
    try {
      //servletContextHandler.addServlet(new ServletHolder(new AdminServlet()), "/api");
      servletContextHandler.addServlet(new ServletHolder(new MetricsServlet(metrics)), "/api/metrics");
      //servletContextHandler.addServlet(new ServletHolder(new ThreadDumpServlet()), "/api/threads");
      //servletContextHandler.addServlet(new ServletHolder(new HealthCheckServlet()), "/api/healthcheck");
      //servletContextHandler.addServlet(new ServletHolder(new PingServlet()), "/api/ping");
    }catch{
      case exception:Exception => { exception.printStackTrace()}
    }

    // adding the configured servlet context handler to the Jetty Server
    server.setHandler(servletContextHandler);
  }

  def start(): Unit = {
    try{
      if (server == null) {
        init()
      }
      logger.info(s"server start host:{} port:{}",bindAddress,port)
      server.start()
    }catch{
      case exception:Exception =>{
         logger.error("启动server异常",exception)
      }
    }
  }

  def stop(): Unit = {
     try{
       if (server != null) {
         server.stop()
       }
     }catch{
       case exception:Exception =>{
           logger.error("停止server异常",exception)
       }
     }
  }

}
