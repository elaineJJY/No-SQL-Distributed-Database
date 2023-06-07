package de.tum.grpc_api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.1)",
    comments = "Source: ECS.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ECSServiceGrpc {

  private ECSServiceGrpc() {}

  public static final String SERVICE_NAME = "ECSService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RegisterRequest,
      de.tum.grpc_api.ECSProto.RegisterResponse> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "register",
      requestType = de.tum.grpc_api.ECSProto.RegisterRequest.class,
      responseType = de.tum.grpc_api.ECSProto.RegisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RegisterRequest,
      de.tum.grpc_api.ECSProto.RegisterResponse> getRegisterMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RegisterRequest, de.tum.grpc_api.ECSProto.RegisterResponse> getRegisterMethod;
    if ((getRegisterMethod = ECSServiceGrpc.getRegisterMethod) == null) {
      synchronized (ECSServiceGrpc.class) {
        if ((getRegisterMethod = ECSServiceGrpc.getRegisterMethod) == null) {
          ECSServiceGrpc.getRegisterMethod = getRegisterMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.RegisterRequest, de.tum.grpc_api.ECSProto.RegisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.RegisterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ECSServiceMethodDescriptorSupplier("register"))
              .build();
        }
      }
    }
    return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRequest,
      de.tum.grpc_api.ECSProto.UpdateResponse> getUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "update",
      requestType = de.tum.grpc_api.ECSProto.UpdateRequest.class,
      responseType = de.tum.grpc_api.ECSProto.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRequest,
      de.tum.grpc_api.ECSProto.UpdateResponse> getUpdateMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.UpdateRequest, de.tum.grpc_api.ECSProto.UpdateResponse> getUpdateMethod;
    if ((getUpdateMethod = ECSServiceGrpc.getUpdateMethod) == null) {
      synchronized (ECSServiceGrpc.class) {
        if ((getUpdateMethod = ECSServiceGrpc.getUpdateMethod) == null) {
          ECSServiceGrpc.getUpdateMethod = getUpdateMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.UpdateRequest, de.tum.grpc_api.ECSProto.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "update"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ECSServiceMethodDescriptorSupplier("update"))
              .build();
        }
      }
    }
    return getUpdateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ECSServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ECSServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ECSServiceStub>() {
        @java.lang.Override
        public ECSServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ECSServiceStub(channel, callOptions);
        }
      };
    return ECSServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ECSServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ECSServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ECSServiceBlockingStub>() {
        @java.lang.Override
        public ECSServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ECSServiceBlockingStub(channel, callOptions);
        }
      };
    return ECSServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ECSServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ECSServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ECSServiceFutureStub>() {
        @java.lang.Override
        public ECSServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ECSServiceFutureStub(channel, callOptions);
        }
      };
    return ECSServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * register new KVServer to ECS
     * </pre>
     */
    default void register(de.tum.grpc_api.ECSProto.RegisterRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.RegisterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    default void update(de.tum.grpc_api.ECSProto.UpdateRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.UpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ECSService.
   */
  public static abstract class ECSServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ECSServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ECSService.
   */
  public static final class ECSServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ECSServiceStub> {
    private ECSServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ECSServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ECSServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * register new KVServer to ECS
     * </pre>
     */
    public void register(de.tum.grpc_api.ECSProto.RegisterRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.RegisterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    public void update(de.tum.grpc_api.ECSProto.UpdateRequest request,
        io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.UpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ECSService.
   */
  public static final class ECSServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ECSServiceBlockingStub> {
    private ECSServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ECSServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ECSServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * register new KVServer to ECS
     * </pre>
     */
    public de.tum.grpc_api.ECSProto.RegisterResponse register(de.tum.grpc_api.ECSProto.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    public de.tum.grpc_api.ECSProto.UpdateResponse update(de.tum.grpc_api.ECSProto.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ECSService.
   */
  public static final class ECSServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ECSServiceFutureStub> {
    private ECSServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ECSServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ECSServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * register new KVServer to ECS
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.RegisterResponse> register(
        de.tum.grpc_api.ECSProto.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.grpc_api.ECSProto.UpdateResponse> update(
        de.tum.grpc_api.ECSProto.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER = 0;
  private static final int METHODID_UPDATE = 1;

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
        case METHODID_REGISTER:
          serviceImpl.register((de.tum.grpc_api.ECSProto.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.RegisterResponse>) responseObserver);
          break;
        case METHODID_UPDATE:
          serviceImpl.update((de.tum.grpc_api.ECSProto.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<de.tum.grpc_api.ECSProto.UpdateResponse>) responseObserver);
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
          getRegisterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.RegisterRequest,
              de.tum.grpc_api.ECSProto.RegisterResponse>(
                service, METHODID_REGISTER)))
        .addMethod(
          getUpdateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.UpdateRequest,
              de.tum.grpc_api.ECSProto.UpdateResponse>(
                service, METHODID_UPDATE)))
        .build();
  }

  private static abstract class ECSServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ECSServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return de.tum.grpc_api.ECSProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ECSService");
    }
  }

  private static final class ECSServiceFileDescriptorSupplier
      extends ECSServiceBaseDescriptorSupplier {
    ECSServiceFileDescriptorSupplier() {}
  }

  private static final class ECSServiceMethodDescriptorSupplier
      extends ECSServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ECSServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ECSServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ECSServiceFileDescriptorSupplier())
              .addMethod(getRegisterMethod())
              .addMethod(getUpdateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
