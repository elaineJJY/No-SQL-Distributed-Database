package de.tum.grpc_api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.1)",
    comments = "Source: ECS.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class KVServiceGrpc {

  private KVServiceGrpc() {}

  public static final String SERVICE_NAME = "KVService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      de.tum.grpc_api.ECSProto.HeartBeatResponse> getHeartBeatRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "heartBeatRPC",
      requestType = com.google.protobuf.Empty.class,
      responseType = de.tum.grpc_api.ECSProto.HeartBeatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      de.tum.grpc_api.ECSProto.HeartBeatResponse> getHeartBeatRPCMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.HeartBeatResponse> getHeartBeatRPCMethod;
    if ((getHeartBeatRPCMethod = KVServiceGrpc.getHeartBeatRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getHeartBeatRPCMethod = KVServiceGrpc.getHeartBeatRPCMethod) == null) {
          KVServiceGrpc.getHeartBeatRPCMethod = getHeartBeatRPCMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.HeartBeatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "heartBeatRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.HeartBeatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("heartBeatRPC"))
              .build();
        }
      }
    }
    return getHeartBeatRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getGetRangeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRange",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getGetRangeMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getGetRangeMethod;
    if ((getGetRangeMethod = KVServiceGrpc.getGetRangeMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getGetRangeMethod = KVServiceGrpc.getGetRangeMethod) == null) {
          KVServiceGrpc.getGetRangeMethod = getGetRangeMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRange"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("getRange"))
              .build();
        }
      }
    }
    return getGetRangeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getEqualsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "equals",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getEqualsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getEqualsMethod;
    if ((getEqualsMethod = KVServiceGrpc.getEqualsMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getEqualsMethod = KVServiceGrpc.getEqualsMethod) == null) {
          KVServiceGrpc.getEqualsMethod = getEqualsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "equals"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("equals"))
              .build();
        }
      }
    }
    return getEqualsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      de.tum.grpc_api.ECSProto.ToStringResponse> getToStringRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "toStringRPC",
      requestType = com.google.protobuf.Empty.class,
      responseType = de.tum.grpc_api.ECSProto.ToStringResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      de.tum.grpc_api.ECSProto.ToStringResponse> getToStringRPCMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.ToStringResponse> getToStringRPCMethod;
    if ((getToStringRPCMethod = KVServiceGrpc.getToStringRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getToStringRPCMethod = KVServiceGrpc.getToStringRPCMethod) == null) {
          KVServiceGrpc.getToStringRPCMethod = getToStringRPCMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.ToStringResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "toStringRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.ToStringResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("toStringRPC"))
              .build();
        }
      }
    }
    return getToStringRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.IsResponsibleRequest,
      de.tum.grpc_api.ECSProto.IsResponsibleResponse> getIsResponsibleRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "isResponsibleRPC",
      requestType = de.tum.grpc_api.ECSProto.IsResponsibleRequest.class,
      responseType = de.tum.grpc_api.ECSProto.IsResponsibleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.IsResponsibleRequest,
      de.tum.grpc_api.ECSProto.IsResponsibleResponse> getIsResponsibleRPCMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.IsResponsibleRequest, de.tum.grpc_api.ECSProto.IsResponsibleResponse> getIsResponsibleRPCMethod;
    if ((getIsResponsibleRPCMethod = KVServiceGrpc.getIsResponsibleRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getIsResponsibleRPCMethod = KVServiceGrpc.getIsResponsibleRPCMethod) == null) {
          KVServiceGrpc.getIsResponsibleRPCMethod = getIsResponsibleRPCMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.IsResponsibleRequest, de.tum.grpc_api.ECSProto.IsResponsibleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "isResponsibleRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.IsResponsibleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.IsResponsibleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("isResponsibleRPC"))
              .build();
        }
      }
    }
    return getIsResponsibleRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getInitRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "initRPC",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getInitRPCMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getInitRPCMethod;
    if ((getInitRPCMethod = KVServiceGrpc.getInitRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getInitRPCMethod = KVServiceGrpc.getInitRPCMethod) == null) {
          KVServiceGrpc.getInitRPCMethod = getInitRPCMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "initRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("initRPC"))
              .build();
        }
      }
    }
    return getInitRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RecoverRequest,
      com.google.protobuf.Empty> getRecoverRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "recoverRPC",
      requestType = de.tum.grpc_api.ECSProto.RecoverRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RecoverRequest,
      com.google.protobuf.Empty> getRecoverRPCMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RecoverRequest, com.google.protobuf.Empty> getRecoverRPCMethod;
    if ((getRecoverRPCMethod = KVServiceGrpc.getRecoverRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getRecoverRPCMethod = KVServiceGrpc.getRecoverRPCMethod) == null) {
          KVServiceGrpc.getRecoverRPCMethod = getRecoverRPCMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.RecoverRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "recoverRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.RecoverRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("recoverRPC"))
              .build();
        }
      }
    }
    return getRecoverRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRingRequest,
      com.google.protobuf.Empty> getUpdateRingRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateRingRPC",
      requestType = de.tum.grpc_api.ECSProto.UpdateRingRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRingRequest,
      com.google.protobuf.Empty> getUpdateRingRPCMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRingRequest, com.google.protobuf.Empty> getUpdateRingRPCMethod;
    if ((getUpdateRingRPCMethod = KVServiceGrpc.getUpdateRingRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getUpdateRingRPCMethod = KVServiceGrpc.getUpdateRingRPCMethod) == null) {
          KVServiceGrpc.getUpdateRingRPCMethod = getUpdateRingRPCMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.UpdateRingRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateRingRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.UpdateRingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("updateRingRPC"))
              .build();
        }
      }
    }
    return getUpdateRingRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getDeleteExpiredDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deleteExpiredData",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getDeleteExpiredDataMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getDeleteExpiredDataMethod;
    if ((getDeleteExpiredDataMethod = KVServiceGrpc.getDeleteExpiredDataMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getDeleteExpiredDataMethod = KVServiceGrpc.getDeleteExpiredDataMethod) == null) {
          KVServiceGrpc.getDeleteExpiredDataMethod = getDeleteExpiredDataMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deleteExpiredData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("deleteExpiredData"))
              .build();
        }
      }
    }
    return getDeleteExpiredDataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getCopyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "copy",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getCopyMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getCopyMethod;
    if ((getCopyMethod = KVServiceGrpc.getCopyMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getCopyMethod = KVServiceGrpc.getCopyMethod) == null) {
          KVServiceGrpc.getCopyMethod = getCopyMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "copy"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("copy"))
              .build();
        }
      }
    }
    return getCopyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRequest,
      de.tum.grpc_api.ECSProto.GetResponse> getGetRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRPC",
      requestType = de.tum.grpc_api.ECSProto.GetRequest.class,
      responseType = de.tum.grpc_api.ECSProto.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRequest,
      de.tum.grpc_api.ECSProto.GetResponse> getGetRPCMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRequest, de.tum.grpc_api.ECSProto.GetResponse> getGetRPCMethod;
    if ((getGetRPCMethod = KVServiceGrpc.getGetRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getGetRPCMethod = KVServiceGrpc.getGetRPCMethod) == null) {
          KVServiceGrpc.getGetRPCMethod = getGetRPCMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.GetRequest, de.tum.grpc_api.ECSProto.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("getRPC"))
              .build();
        }
      }
    }
    return getGetRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutRequest,
      com.google.protobuf.Empty> getPutRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "putRPC",
      requestType = de.tum.grpc_api.ECSProto.PutRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutRequest,
      com.google.protobuf.Empty> getPutRPCMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutRequest, com.google.protobuf.Empty> getPutRPCMethod;
    if ((getPutRPCMethod = KVServiceGrpc.getPutRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getPutRPCMethod = KVServiceGrpc.getPutRPCMethod) == null) {
          KVServiceGrpc.getPutRPCMethod = getPutRPCMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.PutRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "putRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.PutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("putRPC"))
              .build();
        }
      }
    }
    return getPutRPCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteRequest,
      com.google.protobuf.Empty> getDeleteRPCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deleteRPC",
      requestType = de.tum.grpc_api.ECSProto.DeleteRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteRequest,
      com.google.protobuf.Empty> getDeleteRPCMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteRequest, com.google.protobuf.Empty> getDeleteRPCMethod;
    if ((getDeleteRPCMethod = KVServiceGrpc.getDeleteRPCMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getDeleteRPCMethod = KVServiceGrpc.getDeleteRPCMethod) == null) {
          KVServiceGrpc.getDeleteRPCMethod = getDeleteRPCMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.DeleteRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deleteRPC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("deleteRPC"))
              .build();
        }
      }
    }
    return getDeleteRPCMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KVServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KVServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KVServiceStub>() {
        @java.lang.Override
        public KVServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KVServiceStub(channel, callOptions);
        }
      };
    return KVServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KVServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KVServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KVServiceBlockingStub>() {
        @java.lang.Override
        public KVServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KVServiceBlockingStub(channel, callOptions);
        }
      };
    return KVServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KVServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KVServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KVServiceFutureStub>() {
        @java.lang.Override
        public KVServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KVServiceFutureStub(channel, callOptions);
        }
      };
    return KVServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     *  rpc getPort(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     *  rpc getHost(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     * </pre>
     */
    default void heartBeatRPC(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HeartBeatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHeartBeatRPCMethod(), responseObserver);
    }

    /**
     */
    default void getRange(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRangeMethod(), responseObserver);
    }

    /**
     */
    default void equals(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEqualsMethod(), responseObserver);
    }

    /**
     */
    default void toStringRPC(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.ToStringResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getToStringRPCMethod(), responseObserver);
    }

    /**
     */
    default void isResponsibleRPC(de.tum.grpc_api.ECSProto.IsResponsibleRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.IsResponsibleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIsResponsibleRPCMethod(), responseObserver);
    }

    /**
     */
    default void initRPC(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitRPCMethod(), responseObserver);
    }

    /**
     */
    default void recoverRPC(de.tum.grpc_api.ECSProto.RecoverRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecoverRPCMethod(), responseObserver);
    }

    /**
     */
    default void updateRingRPC(de.tum.grpc_api.ECSProto.UpdateRingRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateRingRPCMethod(), responseObserver);
    }

    /**
     */
    default void deleteExpiredData(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteExpiredDataMethod(), responseObserver);
    }

    /**
     */
    default void copy(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCopyMethod(), responseObserver);
    }

    /**
     */
    default void getRPC(de.tum.grpc_api.ECSProto.GetRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRPCMethod(), responseObserver);
    }

    /**
     */
    default void putRPC(de.tum.grpc_api.ECSProto.PutRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutRPCMethod(), responseObserver);
    }

    /**
     */
    default void deleteRPC(de.tum.grpc_api.ECSProto.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteRPCMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service KVService.
   */
  public static abstract class KVServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return KVServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service KVService.
   */
  public static final class KVServiceStub
      extends io.grpc.stub.AbstractAsyncStub<KVServiceStub> {
    private KVServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KVServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *  rpc getPort(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     *  rpc getHost(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     * </pre>
     */
    public void heartBeatRPC(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HeartBeatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHeartBeatRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRange(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRangeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void equals(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEqualsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void toStringRPC(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.ToStringResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getToStringRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void isResponsibleRPC(de.tum.grpc_api.ECSProto.IsResponsibleRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.IsResponsibleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIsResponsibleRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void initRPC(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recoverRPC(de.tum.grpc_api.ECSProto.RecoverRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecoverRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateRingRPC(de.tum.grpc_api.ECSProto.UpdateRingRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateRingRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteExpiredData(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteExpiredDataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void copy(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCopyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRPC(de.tum.grpc_api.ECSProto.GetRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void putRPC(de.tum.grpc_api.ECSProto.PutRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutRPCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteRPC(de.tum.grpc_api.ECSProto.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteRPCMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service KVService.
   */
  public static final class KVServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<KVServiceBlockingStub> {
    private KVServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KVServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *  rpc getPort(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     *  rpc getHost(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     * </pre>
     */
    public de.tum.grpc_api.ECSProto.HeartBeatResponse heartBeatRPC(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHeartBeatRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty getRange(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRangeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty equals(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEqualsMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.ToStringResponse toStringRPC(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getToStringRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.IsResponsibleResponse isResponsibleRPC(de.tum.grpc_api.ECSProto.IsResponsibleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsResponsibleRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty initRPC(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty recoverRPC(de.tum.grpc_api.ECSProto.RecoverRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecoverRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty updateRingRPC(de.tum.grpc_api.ECSProto.UpdateRingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateRingRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteExpiredData(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteExpiredDataMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty copy(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCopyMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.GetResponse getRPC(de.tum.grpc_api.ECSProto.GetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty putRPC(de.tum.grpc_api.ECSProto.PutRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutRPCMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteRPC(de.tum.grpc_api.ECSProto.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteRPCMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service KVService.
   */
  public static final class KVServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<KVServiceFutureStub> {
    private KVServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KVServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KVServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *  rpc getPort(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     *  rpc getHost(google.protobuf.Empty) returns (google.protobuf.Empty) {}
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.HeartBeatResponse> heartBeatRPC(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHeartBeatRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> getRange(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRangeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> equals(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEqualsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.ToStringResponse> toStringRPC(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getToStringRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.IsResponsibleResponse> isResponsibleRPC(
        de.tum.grpc_api.ECSProto.IsResponsibleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIsResponsibleRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> initRPC(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> recoverRPC(
        de.tum.grpc_api.ECSProto.RecoverRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecoverRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> updateRingRPC(
        de.tum.grpc_api.ECSProto.UpdateRingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateRingRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteExpiredData(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteExpiredDataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> copy(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCopyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.GetResponse> getRPC(
        de.tum.grpc_api.ECSProto.GetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> putRPC(
        de.tum.grpc_api.ECSProto.PutRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutRPCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteRPC(
        de.tum.grpc_api.ECSProto.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteRPCMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HEART_BEAT_RPC = 0;
  private static final int METHODID_GET_RANGE = 1;
  private static final int METHODID_EQUALS = 2;
  private static final int METHODID_TO_STRING_RPC = 3;
  private static final int METHODID_IS_RESPONSIBLE_RPC = 4;
  private static final int METHODID_INIT_RPC = 5;
  private static final int METHODID_RECOVER_RPC = 6;
  private static final int METHODID_UPDATE_RING_RPC = 7;
  private static final int METHODID_DELETE_EXPIRED_DATA = 8;
  private static final int METHODID_COPY = 9;
  private static final int METHODID_GET_RPC = 10;
  private static final int METHODID_PUT_RPC = 11;
  private static final int METHODID_DELETE_RPC = 12;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HEART_BEAT_RPC:
          serviceImpl.heartBeatRPC((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HeartBeatResponse>) responseObserver);
          break;
        case METHODID_GET_RANGE:
          serviceImpl.getRange((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_EQUALS:
          serviceImpl.equals((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_TO_STRING_RPC:
          serviceImpl.toStringRPC((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.ToStringResponse>) responseObserver);
          break;
        case METHODID_IS_RESPONSIBLE_RPC:
          serviceImpl.isResponsibleRPC((de.tum.grpc_api.ECSProto.IsResponsibleRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.IsResponsibleResponse>) responseObserver);
          break;
        case METHODID_INIT_RPC:
          serviceImpl.initRPC((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_RECOVER_RPC:
          serviceImpl.recoverRPC((de.tum.grpc_api.ECSProto.RecoverRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_UPDATE_RING_RPC:
          serviceImpl.updateRingRPC((de.tum.grpc_api.ECSProto.UpdateRingRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_DELETE_EXPIRED_DATA:
          serviceImpl.deleteExpiredData((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_COPY:
          serviceImpl.copy((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET_RPC:
          serviceImpl.getRPC((de.tum.grpc_api.ECSProto.GetRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetResponse>) responseObserver);
          break;
        case METHODID_PUT_RPC:
          serviceImpl.putRPC((de.tum.grpc_api.ECSProto.PutRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_DELETE_RPC:
          serviceImpl.deleteRPC((de.tum.grpc_api.ECSProto.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getHeartBeatRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              de.tum.grpc_api.ECSProto.HeartBeatResponse>(
                service, METHODID_HEART_BEAT_RPC)))
        .addMethod(
          getGetRangeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_GET_RANGE)))
        .addMethod(
          getEqualsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_EQUALS)))
        .addMethod(
          getToStringRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              de.tum.grpc_api.ECSProto.ToStringResponse>(
                service, METHODID_TO_STRING_RPC)))
        .addMethod(
          getIsResponsibleRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.IsResponsibleRequest,
              de.tum.grpc_api.ECSProto.IsResponsibleResponse>(
                service, METHODID_IS_RESPONSIBLE_RPC)))
        .addMethod(
          getInitRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_INIT_RPC)))
        .addMethod(
          getRecoverRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.RecoverRequest,
              com.google.protobuf.Empty>(
                service, METHODID_RECOVER_RPC)))
        .addMethod(
          getUpdateRingRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.UpdateRingRequest,
              com.google.protobuf.Empty>(
                service, METHODID_UPDATE_RING_RPC)))
        .addMethod(
          getDeleteExpiredDataMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE_EXPIRED_DATA)))
        .addMethod(
          getCopyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_COPY)))
        .addMethod(
          getGetRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.GetRequest,
              de.tum.grpc_api.ECSProto.GetResponse>(
                service, METHODID_GET_RPC)))
        .addMethod(
          getPutRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.PutRequest,
              com.google.protobuf.Empty>(
                service, METHODID_PUT_RPC)))
        .addMethod(
          getDeleteRPCMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.DeleteRequest,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE_RPC)))
        .build();
  }

  private static abstract class KVServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KVServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return de.tum.grpc_api.ECSProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("KVService");
    }
  }

  private static final class KVServiceFileDescriptorSupplier
      extends KVServiceBaseDescriptorSupplier {
    KVServiceFileDescriptorSupplier() {}
  }

  private static final class KVServiceMethodDescriptorSupplier
      extends KVServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    KVServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (KVServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KVServiceFileDescriptorSupplier())
              .addMethod(getHeartBeatRPCMethod())
              .addMethod(getGetRangeMethod())
              .addMethod(getEqualsMethod())
              .addMethod(getToStringRPCMethod())
              .addMethod(getIsResponsibleRPCMethod())
              .addMethod(getInitRPCMethod())
              .addMethod(getRecoverRPCMethod())
              .addMethod(getUpdateRingRPCMethod())
              .addMethod(getDeleteExpiredDataMethod())
              .addMethod(getCopyMethod())
              .addMethod(getGetRPCMethod())
              .addMethod(getPutRPCMethod())
              .addMethod(getDeleteRPCMethod())
              .build();
        }
      }
    }
    return result;
  }
}
