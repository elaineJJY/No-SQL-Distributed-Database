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
      de.tum.grpc_api.ECSProto.HeartBeatResponse> getHeartBeatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "heartBeat",
      requestType = com.google.protobuf.Empty.class,
      responseType = de.tum.grpc_api.ECSProto.HeartBeatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      de.tum.grpc_api.ECSProto.HeartBeatResponse> getHeartBeatMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.HeartBeatResponse> getHeartBeatMethod;
    if ((getHeartBeatMethod = KVServiceGrpc.getHeartBeatMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getHeartBeatMethod = KVServiceGrpc.getHeartBeatMethod) == null) {
          KVServiceGrpc.getHeartBeatMethod = getHeartBeatMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.HeartBeatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "heartBeat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.HeartBeatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("heartBeat"))
              .build();
        }
      }
    }
    return getHeartBeatMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRangeRequest,
      de.tum.grpc_api.ECSProto.GetRangeResponse> getGetRangeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRange",
      requestType = de.tum.grpc_api.ECSProto.GetRangeRequest.class,
      responseType = de.tum.grpc_api.ECSProto.GetRangeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRangeRequest,
      de.tum.grpc_api.ECSProto.GetRangeResponse> getGetRangeMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRangeRequest, de.tum.grpc_api.ECSProto.GetRangeResponse> getGetRangeMethod;
    if ((getGetRangeMethod = KVServiceGrpc.getGetRangeMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getGetRangeMethod = KVServiceGrpc.getGetRangeMethod) == null) {
          KVServiceGrpc.getGetRangeMethod = getGetRangeMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.GetRangeRequest, de.tum.grpc_api.ECSProto.GetRangeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRange"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.GetRangeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.GetRangeResponse.getDefaultInstance()))
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
      de.tum.grpc_api.ECSProto.ToStringResponse> getToStringMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "toString",
      requestType = com.google.protobuf.Empty.class,
      responseType = de.tum.grpc_api.ECSProto.ToStringResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      de.tum.grpc_api.ECSProto.ToStringResponse> getToStringMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.ToStringResponse> getToStringMethod;
    if ((getToStringMethod = KVServiceGrpc.getToStringMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getToStringMethod = KVServiceGrpc.getToStringMethod) == null) {
          KVServiceGrpc.getToStringMethod = getToStringMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, de.tum.grpc_api.ECSProto.ToStringResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "toString"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.ToStringResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("toString"))
              .build();
        }
      }
    }
    return getToStringMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.IsResponsibleRequest,
      de.tum.grpc_api.ECSProto.IsResponsibleResponse> getIsResponsibleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "isResponsible",
      requestType = de.tum.grpc_api.ECSProto.IsResponsibleRequest.class,
      responseType = de.tum.grpc_api.ECSProto.IsResponsibleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.IsResponsibleRequest,
      de.tum.grpc_api.ECSProto.IsResponsibleResponse> getIsResponsibleMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.IsResponsibleRequest, de.tum.grpc_api.ECSProto.IsResponsibleResponse> getIsResponsibleMethod;
    if ((getIsResponsibleMethod = KVServiceGrpc.getIsResponsibleMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getIsResponsibleMethod = KVServiceGrpc.getIsResponsibleMethod) == null) {
          KVServiceGrpc.getIsResponsibleMethod = getIsResponsibleMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.IsResponsibleRequest, de.tum.grpc_api.ECSProto.IsResponsibleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "isResponsible"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.IsResponsibleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.IsResponsibleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("isResponsible"))
              .build();
        }
      }
    }
    return getIsResponsibleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.CopyRequest,
      de.tum.grpc_api.ECSProto.CopyResponse> getCopyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "copy",
      requestType = de.tum.grpc_api.ECSProto.CopyRequest.class,
      responseType = de.tum.grpc_api.ECSProto.CopyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.CopyRequest,
      de.tum.grpc_api.ECSProto.CopyResponse> getCopyMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.CopyRequest, de.tum.grpc_api.ECSProto.CopyResponse> getCopyMethod;
    if ((getCopyMethod = KVServiceGrpc.getCopyMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getCopyMethod = KVServiceGrpc.getCopyMethod) == null) {
          KVServiceGrpc.getCopyMethod = getCopyMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.CopyRequest, de.tum.grpc_api.ECSProto.CopyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "copy"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.CopyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.CopyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("copy"))
              .build();
        }
      }
    }
    return getCopyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRequest,
      de.tum.grpc_api.ECSProto.GetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get",
      requestType = de.tum.grpc_api.ECSProto.GetRequest.class,
      responseType = de.tum.grpc_api.ECSProto.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRequest,
      de.tum.grpc_api.ECSProto.GetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.GetRequest, de.tum.grpc_api.ECSProto.GetResponse> getGetMethod;
    if ((getGetMethod = KVServiceGrpc.getGetMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getGetMethod = KVServiceGrpc.getGetMethod) == null) {
          KVServiceGrpc.getGetMethod = getGetMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.GetRequest, de.tum.grpc_api.ECSProto.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("get"))
              .build();
        }
      }
    }
    return getGetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutRequest,
      com.google.protobuf.Empty> getPutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "put",
      requestType = de.tum.grpc_api.ECSProto.PutRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutRequest,
      com.google.protobuf.Empty> getPutMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutRequest, com.google.protobuf.Empty> getPutMethod;
    if ((getPutMethod = KVServiceGrpc.getPutMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getPutMethod = KVServiceGrpc.getPutMethod) == null) {
          KVServiceGrpc.getPutMethod = getPutMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.PutRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "put"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.PutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("put"))
              .build();
        }
      }
    }
    return getPutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutBackupRequest,
      com.google.protobuf.Empty> getPutBackupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "putBackup",
      requestType = de.tum.grpc_api.ECSProto.PutBackupRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutBackupRequest,
      com.google.protobuf.Empty> getPutBackupMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.PutBackupRequest, com.google.protobuf.Empty> getPutBackupMethod;
    if ((getPutBackupMethod = KVServiceGrpc.getPutBackupMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getPutBackupMethod = KVServiceGrpc.getPutBackupMethod) == null) {
          KVServiceGrpc.getPutBackupMethod = getPutBackupMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.PutBackupRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "putBackup"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.PutBackupRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("putBackup"))
              .build();
        }
      }
    }
    return getPutBackupMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteRequest,
      com.google.protobuf.Empty> getDeleteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "delete",
      requestType = de.tum.grpc_api.ECSProto.DeleteRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteRequest,
      com.google.protobuf.Empty> getDeleteMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteRequest, com.google.protobuf.Empty> getDeleteMethod;
    if ((getDeleteMethod = KVServiceGrpc.getDeleteMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getDeleteMethod = KVServiceGrpc.getDeleteMethod) == null) {
          KVServiceGrpc.getDeleteMethod = getDeleteMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.DeleteRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "delete"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("delete"))
              .build();
        }
      }
    }
    return getDeleteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.HasKeyRequest,
      de.tum.grpc_api.ECSProto.HasKeyResponse> getHasKeyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hasKey",
      requestType = de.tum.grpc_api.ECSProto.HasKeyRequest.class,
      responseType = de.tum.grpc_api.ECSProto.HasKeyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.HasKeyRequest,
      de.tum.grpc_api.ECSProto.HasKeyResponse> getHasKeyMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.HasKeyRequest, de.tum.grpc_api.ECSProto.HasKeyResponse> getHasKeyMethod;
    if ((getHasKeyMethod = KVServiceGrpc.getHasKeyMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getHasKeyMethod = KVServiceGrpc.getHasKeyMethod) == null) {
          KVServiceGrpc.getHasKeyMethod = getHasKeyMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.HasKeyRequest, de.tum.grpc_api.ECSProto.HasKeyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hasKey"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.HasKeyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.HasKeyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("hasKey"))
              .build();
        }
      }
    }
    return getHasKeyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getInitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "init",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getInitMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getInitMethod;
    if ((getInitMethod = KVServiceGrpc.getInitMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getInitMethod = KVServiceGrpc.getInitMethod) == null) {
          KVServiceGrpc.getInitMethod = getInitMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "init"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("init"))
              .build();
        }
      }
    }
    return getInitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getStartKVServerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "startKVServer",
      requestType = com.google.protobuf.Empty.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      com.google.protobuf.Empty> getStartKVServerMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, com.google.protobuf.Empty> getStartKVServerMethod;
    if ((getStartKVServerMethod = KVServiceGrpc.getStartKVServerMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getStartKVServerMethod = KVServiceGrpc.getStartKVServerMethod) == null) {
          KVServiceGrpc.getStartKVServerMethod = getStartKVServerMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "startKVServer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("startKVServer"))
              .build();
        }
      }
    }
    return getStartKVServerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RecoverRequest,
      com.google.protobuf.Empty> getRecoverMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "recover",
      requestType = de.tum.grpc_api.ECSProto.RecoverRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RecoverRequest,
      com.google.protobuf.Empty> getRecoverMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RecoverRequest, com.google.protobuf.Empty> getRecoverMethod;
    if ((getRecoverMethod = KVServiceGrpc.getRecoverMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getRecoverMethod = KVServiceGrpc.getRecoverMethod) == null) {
          KVServiceGrpc.getRecoverMethod = getRecoverMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.RecoverRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "recover"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.RecoverRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("recover"))
              .build();
        }
      }
    }
    return getRecoverMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRingRequest,
      com.google.protobuf.Empty> getUpdateRingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateRing",
      requestType = de.tum.grpc_api.ECSProto.UpdateRingRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRingRequest,
      com.google.protobuf.Empty> getUpdateRingMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRingRequest, com.google.protobuf.Empty> getUpdateRingMethod;
    if ((getUpdateRingMethod = KVServiceGrpc.getUpdateRingMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getUpdateRingMethod = KVServiceGrpc.getUpdateRingMethod) == null) {
          KVServiceGrpc.getUpdateRingMethod = getUpdateRingMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.UpdateRingRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateRing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.UpdateRingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("updateRing"))
              .build();
        }
      }
    }
    return getUpdateRingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest,
      com.google.protobuf.Empty> getDeleteExpiredDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deleteExpiredData",
      requestType = de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest,
      com.google.protobuf.Empty> getDeleteExpiredDataMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest, com.google.protobuf.Empty> getDeleteExpiredDataMethod;
    if ((getDeleteExpiredDataMethod = KVServiceGrpc.getDeleteExpiredDataMethod) == null) {
      synchronized (KVServiceGrpc.class) {
        if ((getDeleteExpiredDataMethod = KVServiceGrpc.getDeleteExpiredDataMethod) == null) {
          KVServiceGrpc.getDeleteExpiredDataMethod = getDeleteExpiredDataMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deleteExpiredData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new KVServiceMethodDescriptorSupplier("deleteExpiredData"))
              .build();
        }
      }
    }
    return getDeleteExpiredDataMethod;
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
     */
    default void heartBeat(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HeartBeatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHeartBeatMethod(), responseObserver);
    }

    /**
     */
    default void getRange(de.tum.grpc_api.ECSProto.GetRangeRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetRangeResponse> responseObserver) {
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
    default void toString(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.ToStringResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getToStringMethod(), responseObserver);
    }

    /**
     * <pre>
     * isResponsible, copy, get, put, delete will only be called by other KVServer
     * </pre>
     */
    default void isResponsible(de.tum.grpc_api.ECSProto.IsResponsibleRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.IsResponsibleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIsResponsibleMethod(), responseObserver);
    }

    /**
     */
    default void copy(de.tum.grpc_api.ECSProto.CopyRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.CopyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCopyMethod(), responseObserver);
    }

    /**
     */
    default void get(de.tum.grpc_api.ECSProto.GetRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }

    /**
     */
    default void put(de.tum.grpc_api.ECSProto.PutRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutMethod(), responseObserver);
    }

    /**
     */
    default void putBackup(de.tum.grpc_api.ECSProto.PutBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPutBackupMethod(), responseObserver);
    }

    /**
     */
    default void delete(de.tum.grpc_api.ECSProto.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMethod(), responseObserver);
    }

    /**
     */
    default void hasKey(de.tum.grpc_api.ECSProto.HasKeyRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HasKeyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHasKeyMethod(), responseObserver);
    }

    /**
     * <pre>
     * init, recover, updateRing, deleteExpiredData will only be called by ECS
     * </pre>
     */
    default void init(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitMethod(), responseObserver);
    }

    /**
     */
    default void startKVServer(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStartKVServerMethod(), responseObserver);
    }

    /**
     */
    default void recover(de.tum.grpc_api.ECSProto.RecoverRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecoverMethod(), responseObserver);
    }

    /**
     */
    default void updateRing(de.tum.grpc_api.ECSProto.UpdateRingRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateRingMethod(), responseObserver);
    }

    /**
     */
    default void deleteExpiredData(de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteExpiredDataMethod(), responseObserver);
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
     */
    public void heartBeat(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HeartBeatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHeartBeatMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRange(de.tum.grpc_api.ECSProto.GetRangeRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetRangeResponse> responseObserver) {
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
    public void toString(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.ToStringResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getToStringMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * isResponsible, copy, get, put, delete will only be called by other KVServer
     * </pre>
     */
    public void isResponsible(de.tum.grpc_api.ECSProto.IsResponsibleRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.IsResponsibleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIsResponsibleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void copy(de.tum.grpc_api.ECSProto.CopyRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.CopyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCopyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void get(de.tum.grpc_api.ECSProto.GetRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void put(de.tum.grpc_api.ECSProto.PutRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void putBackup(de.tum.grpc_api.ECSProto.PutBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPutBackupMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delete(de.tum.grpc_api.ECSProto.DeleteRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hasKey(de.tum.grpc_api.ECSProto.HasKeyRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HasKeyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHasKeyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * init, recover, updateRing, deleteExpiredData will only be called by ECS
     * </pre>
     */
    public void init(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void startKVServer(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStartKVServerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recover(de.tum.grpc_api.ECSProto.RecoverRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRecoverMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateRing(de.tum.grpc_api.ECSProto.UpdateRingRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateRingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteExpiredData(de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteExpiredDataMethod(), getCallOptions()), request, responseObserver);
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
     */
    public de.tum.grpc_api.ECSProto.HeartBeatResponse heartBeat(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHeartBeatMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.GetRangeResponse getRange(de.tum.grpc_api.ECSProto.GetRangeRequest request) {
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
    public de.tum.grpc_api.ECSProto.ToStringResponse toString(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getToStringMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * isResponsible, copy, get, put, delete will only be called by other KVServer
     * </pre>
     */
    public de.tum.grpc_api.ECSProto.IsResponsibleResponse isResponsible(de.tum.grpc_api.ECSProto.IsResponsibleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsResponsibleMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.CopyResponse copy(de.tum.grpc_api.ECSProto.CopyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCopyMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.GetResponse get(de.tum.grpc_api.ECSProto.GetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty put(de.tum.grpc_api.ECSProto.PutRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty putBackup(de.tum.grpc_api.ECSProto.PutBackupRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPutBackupMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty delete(de.tum.grpc_api.ECSProto.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMethod(), getCallOptions(), request);
    }

    /**
     */
    public de.tum.grpc_api.ECSProto.HasKeyResponse hasKey(de.tum.grpc_api.ECSProto.HasKeyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHasKeyMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * init, recover, updateRing, deleteExpiredData will only be called by ECS
     * </pre>
     */
    public com.google.protobuf.Empty init(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty startKVServer(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStartKVServerMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty recover(de.tum.grpc_api.ECSProto.RecoverRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRecoverMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty updateRing(de.tum.grpc_api.ECSProto.UpdateRingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateRingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteExpiredData(de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteExpiredDataMethod(), getCallOptions(), request);
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
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.HeartBeatResponse> heartBeat(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHeartBeatMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.GetRangeResponse> getRange(
        de.tum.grpc_api.ECSProto.GetRangeRequest request) {
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
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.ToStringResponse> toString(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getToStringMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * isResponsible, copy, get, put, delete will only be called by other KVServer
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.IsResponsibleResponse> isResponsible(
        de.tum.grpc_api.ECSProto.IsResponsibleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIsResponsibleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.CopyResponse> copy(
        de.tum.grpc_api.ECSProto.CopyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCopyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.GetResponse> get(
        de.tum.grpc_api.ECSProto.GetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> put(
        de.tum.grpc_api.ECSProto.PutRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> putBackup(
        de.tum.grpc_api.ECSProto.PutBackupRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPutBackupMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> delete(
        de.tum.grpc_api.ECSProto.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.HasKeyResponse> hasKey(
        de.tum.grpc_api.ECSProto.HasKeyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHasKeyMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * init, recover, updateRing, deleteExpiredData will only be called by ECS
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> init(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> startKVServer(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStartKVServerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> recover(
        de.tum.grpc_api.ECSProto.RecoverRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRecoverMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> updateRing(
        de.tum.grpc_api.ECSProto.UpdateRingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateRingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteExpiredData(
        de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteExpiredDataMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HEART_BEAT = 0;
  private static final int METHODID_GET_RANGE = 1;
  private static final int METHODID_EQUALS = 2;
  private static final int METHODID_TO_STRING = 3;
  private static final int METHODID_IS_RESPONSIBLE = 4;
  private static final int METHODID_COPY = 5;
  private static final int METHODID_GET = 6;
  private static final int METHODID_PUT = 7;
  private static final int METHODID_PUT_BACKUP = 8;
  private static final int METHODID_DELETE = 9;
  private static final int METHODID_HAS_KEY = 10;
  private static final int METHODID_INIT = 11;
  private static final int METHODID_START_KVSERVER = 12;
  private static final int METHODID_RECOVER = 13;
  private static final int METHODID_UPDATE_RING = 14;
  private static final int METHODID_DELETE_EXPIRED_DATA = 15;

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
        case METHODID_HEART_BEAT:
          serviceImpl.heartBeat((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HeartBeatResponse>) responseObserver);
          break;
        case METHODID_GET_RANGE:
          serviceImpl.getRange((de.tum.grpc_api.ECSProto.GetRangeRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetRangeResponse>) responseObserver);
          break;
        case METHODID_EQUALS:
          serviceImpl.equals((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_TO_STRING:
          serviceImpl.toString((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.ToStringResponse>) responseObserver);
          break;
        case METHODID_IS_RESPONSIBLE:
          serviceImpl.isResponsible((de.tum.grpc_api.ECSProto.IsResponsibleRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.IsResponsibleResponse>) responseObserver);
          break;
        case METHODID_COPY:
          serviceImpl.copy((de.tum.grpc_api.ECSProto.CopyRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.CopyResponse>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((de.tum.grpc_api.ECSProto.GetRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.GetResponse>) responseObserver);
          break;
        case METHODID_PUT:
          serviceImpl.put((de.tum.grpc_api.ECSProto.PutRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_PUT_BACKUP:
          serviceImpl.putBackup((de.tum.grpc_api.ECSProto.PutBackupRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((de.tum.grpc_api.ECSProto.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_HAS_KEY:
          serviceImpl.hasKey((de.tum.grpc_api.ECSProto.HasKeyRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.HasKeyResponse>) responseObserver);
          break;
        case METHODID_INIT:
          serviceImpl.init((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_START_KVSERVER:
          serviceImpl.startKVServer((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_RECOVER:
          serviceImpl.recover((de.tum.grpc_api.ECSProto.RecoverRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_UPDATE_RING:
          serviceImpl.updateRing((de.tum.grpc_api.ECSProto.UpdateRingRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_DELETE_EXPIRED_DATA:
          serviceImpl.deleteExpiredData((de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest) request,
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
          getHeartBeatMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              de.tum.grpc_api.ECSProto.HeartBeatResponse>(
                service, METHODID_HEART_BEAT)))
        .addMethod(
          getGetRangeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.GetRangeRequest,
              de.tum.grpc_api.ECSProto.GetRangeResponse>(
                service, METHODID_GET_RANGE)))
        .addMethod(
          getEqualsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_EQUALS)))
        .addMethod(
          getToStringMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              de.tum.grpc_api.ECSProto.ToStringResponse>(
                service, METHODID_TO_STRING)))
        .addMethod(
          getIsResponsibleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.IsResponsibleRequest,
              de.tum.grpc_api.ECSProto.IsResponsibleResponse>(
                service, METHODID_IS_RESPONSIBLE)))
        .addMethod(
          getCopyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.CopyRequest,
              de.tum.grpc_api.ECSProto.CopyResponse>(
                service, METHODID_COPY)))
        .addMethod(
          getGetMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.GetRequest,
              de.tum.grpc_api.ECSProto.GetResponse>(
                service, METHODID_GET)))
        .addMethod(
          getPutMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.PutRequest,
              com.google.protobuf.Empty>(
                service, METHODID_PUT)))
        .addMethod(
          getPutBackupMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.PutBackupRequest,
              com.google.protobuf.Empty>(
                service, METHODID_PUT_BACKUP)))
        .addMethod(
          getDeleteMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.DeleteRequest,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE)))
        .addMethod(
          getHasKeyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.HasKeyRequest,
              de.tum.grpc_api.ECSProto.HasKeyResponse>(
                service, METHODID_HAS_KEY)))
        .addMethod(
          getInitMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_INIT)))
        .addMethod(
          getStartKVServerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              com.google.protobuf.Empty>(
                service, METHODID_START_KVSERVER)))
        .addMethod(
          getRecoverMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.RecoverRequest,
              com.google.protobuf.Empty>(
                service, METHODID_RECOVER)))
        .addMethod(
          getUpdateRingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.UpdateRingRequest,
              com.google.protobuf.Empty>(
                service, METHODID_UPDATE_RING)))
        .addMethod(
          getDeleteExpiredDataMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.DeleteExpiredDataRequest,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE_EXPIRED_DATA)))
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
              .addMethod(getHeartBeatMethod())
              .addMethod(getGetRangeMethod())
              .addMethod(getEqualsMethod())
              .addMethod(getToStringMethod())
              .addMethod(getIsResponsibleMethod())
              .addMethod(getCopyMethod())
              .addMethod(getGetMethod())
              .addMethod(getPutMethod())
              .addMethod(getPutBackupMethod())
              .addMethod(getDeleteMethod())
              .addMethod(getHasKeyMethod())
              .addMethod(getInitMethod())
              .addMethod(getStartKVServerMethod())
              .addMethod(getRecoverMethod())
              .addMethod(getUpdateRingMethod())
              .addMethod(getDeleteExpiredDataMethod())
              .build();
        }
      }
    }
    return result;
  }
}
