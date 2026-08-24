import Testing
import TracingSubscriber

@Suite("TracingSubscriber Swift Export Test Suite")
struct TracingSubscriberExportTests {
    @Test("TracingSubscriber module imports and exposes types")
    func testSwiftModuleLoads() throws {
        let registry = io.github.kotlinmania.tracingsubscriber.registry.registry()
        _ = registry
    }
}

