package de.tum.grpc_api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.1)",
    comments = "Source: ECS.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ECServiceGrpc {

  private ECServiceGrpc() {}

  public static final String SERVICE_NAME = "ECService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RegisterRequest,
      com.google.protobuf.Empty> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "register",
      requestType = de.tum.grpc_api.ECSProto.RegisterRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RegisterRequest,
      com.google.protobuf.Empty> getRegisterMethod() {
    io.grpc.MethodDescriptor<de.tum.grpc_api.ECSProto.RegisterRequest, com.google.protobuf.Empty> getRegisterMethod;
    if ((getRegisterMethod = ECServiceGrpc.getRegisterMethod) == null) {
      synchronized (ECServiceGrpc.class) {
        if ((getRegisterMethod = ECServiceGrpc.getRegisterMethod) == null) {
          ECServiceGrpc.getRegisterMethod = getRegisterMethod =
              io.grpc.MethodDescriptor.<de.tum.grpc_api.ECSProto.RegisterRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.grpc_api.ECSProto.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ECServiceMethodDescriptorSupplier("register"))
              .build();
        }
      }
    }
    return getRegisterMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ECServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ECServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ECServiceStub>() {
        @java.lang.Override
        public ECServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ECServiceStub(channel, callOptions);
        }
      };
    return ECServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ECServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ECServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ECServiceBlockingStub>() {
        @java.lang.Override
        public ECServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ECServiceBlockingStub(channel, callOptions);
        }
      };
    return ECServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ECServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ECServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ECServiceFutureStub>() {
        @java.lang.Override
        public ECServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ECServiceFutureStub(channel, callOptions);
        }
      };
    return ECServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void register(de.tum.grpc_api.ECSProto.RegisterRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ECService.
   */
  public static abstract class ECServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ECServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ECService.
   */
  public static final class ECServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ECServiceStub> {
    private ECServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ECServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ECServiceStub(channel, callOptions);
    }

    /**
     */
    public void register(de.tum.grpc_api.ECSProto.RegisterRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ECService.
   */
  public static final class ECServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ECServiceBlockingStub> {
    private ECServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ECServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ECServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty register(de.tum.grpc_api.ECSProto.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ECService.
   */
  public static final class ECServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ECServiceFutureStub> {
    private ECServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ECServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ECServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> register(
        de.tum.grpc_api.ECSProto.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER = 0;

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
          getRegisterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              de.tum.grpc_api.ECSProto.RegisterRequest,
              com.google.protobuf.Empty>(
                service, METHODID_REGISTER)))
        .build();
  }

  private static abstract class ECServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ECServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return de.tum.grpc_api.ECSProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ECService");
    }
  }

  private static final class ECServiceFileDescriptorSupplier
      extends ECServiceBaseDescriptorSupplier {
    ECServiceFileDescriptorSupplier() {}
  }

  private static final class ECServiceMethodDescriptorSupplier
      extends ECServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ECServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ECServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ECServiceFileDescriptorSupplier())
              .addMethod(getRegisterMethod())
              .build();
        }
      }
    }
    return result;
  }
}
