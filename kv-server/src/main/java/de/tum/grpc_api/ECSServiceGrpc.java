package grpc_api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.1)",
    comments = "Source: KVServer.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ECSServiceGrpc {

  private ECSServiceGrpc() {}

  public static final String SERVICE_NAME = "ECSService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc_api.KVServerProto.InitRequest,
      grpc_api.KVServerProto.InitResponse> getInitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "init",
      requestType = grpc_api.KVServerProto.InitRequest.class,
      responseType = grpc_api.KVServerProto.InitResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc_api.KVServerProto.InitRequest,
      grpc_api.KVServerProto.InitResponse> getInitMethod() {
    io.grpc.MethodDescriptor<grpc_api.KVServerProto.InitRequest, grpc_api.KVServerProto.InitResponse> getInitMethod;
    if ((getInitMethod = ECSServiceGrpc.getInitMethod) == null) {
      synchronized (ECSServiceGrpc.class) {
        if ((getInitMethod = ECSServiceGrpc.getInitMethod) == null) {
          ECSServiceGrpc.getInitMethod = getInitMethod =
              io.grpc.MethodDescriptor.<grpc_api.KVServerProto.InitRequest, grpc_api.KVServerProto.InitResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "init"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc_api.KVServerProto.InitRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc_api.KVServerProto.InitResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ECSServiceMethodDescriptorSupplier("init"))
              .build();
        }
      }
    }
    return getInitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc_api.KVServerProto.UpdateRequest,
      grpc_api.KVServerProto.UpdateResponse> getUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "update",
      requestType = grpc_api.KVServerProto.UpdateRequest.class,
      responseType = grpc_api.KVServerProto.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc_api.KVServerProto.UpdateRequest,
      grpc_api.KVServerProto.UpdateResponse> getUpdateMethod() {
    io.grpc.MethodDescriptor<grpc_api.KVServerProto.UpdateRequest, grpc_api.KVServerProto.UpdateResponse> getUpdateMethod;
    if ((getUpdateMethod = ECSServiceGrpc.getUpdateMethod) == null) {
      synchronized (ECSServiceGrpc.class) {
        if ((getUpdateMethod = ECSServiceGrpc.getUpdateMethod) == null) {
          ECSServiceGrpc.getUpdateMethod = getUpdateMethod =
              io.grpc.MethodDescriptor.<grpc_api.KVServerProto.UpdateRequest, grpc_api.KVServerProto.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "update"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc_api.KVServerProto.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc_api.KVServerProto.UpdateResponse.getDefaultInstance()))
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
     * initialize server
     * </pre>
     */
    default void init(grpc_api.KVServerProto.InitRequest request,
        io.grpc.stub.StreamObserver<grpc_api.KVServerProto.InitResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitMethod(), responseObserver);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    default void update(grpc_api.KVServerProto.UpdateRequest request,
        io.grpc.stub.StreamObserver<grpc_api.KVServerProto.UpdateResponse> responseObserver) {
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
     * initialize server
     * </pre>
     */
    public void init(grpc_api.KVServerProto.InitRequest request,
        io.grpc.stub.StreamObserver<grpc_api.KVServerProto.InitResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    public void update(grpc_api.KVServerProto.UpdateRequest request,
        io.grpc.stub.StreamObserver<grpc_api.KVServerProto.UpdateResponse> responseObserver) {
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
     * initialize server
     * </pre>
     */
    public grpc_api.KVServerProto.InitResponse init(grpc_api.KVServerProto.InitRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    public grpc_api.KVServerProto.UpdateResponse update(grpc_api.KVServerProto.UpdateRequest request) {
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
     * initialize server
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc_api.KVServerProto.InitResponse> init(
        grpc_api.KVServerProto.InitRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *rpc recover(KVRequest) returns (KVResponse);
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc_api.KVServerProto.UpdateResponse> update(
        grpc_api.KVServerProto.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INIT = 0;
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
        case METHODID_INIT:
          serviceImpl.init((grpc_api.KVServerProto.InitRequest) request,
              (io.grpc.stub.StreamObserver<grpc_api.KVServerProto.InitResponse>) responseObserver);
          break;
        case METHODID_UPDATE:
          serviceImpl.update((grpc_api.KVServerProto.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<grpc_api.KVServerProto.UpdateResponse>) responseObserver);
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
          getInitMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              grpc_api.KVServerProto.InitRequest,
              grpc_api.KVServerProto.InitResponse>(
                service, METHODID_INIT)))
        .addMethod(
          getUpdateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              grpc_api.KVServerProto.UpdateRequest,
              grpc_api.KVServerProto.UpdateResponse>(
                service, METHODID_UPDATE)))
        .build();
  }

  private static abstract class ECSServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ECSServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc_api.KVServerProto.getDescriptor();
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
              .addMethod(getInitMethod())
              .addMethod(getUpdateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
