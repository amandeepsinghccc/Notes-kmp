import SwiftUI

@main
struct iOSApp: App {

    init() {
    Main_iosKt.doInitKoin()
    }

    @UIApplicationDelegateAdaptor(AppDelegate.self)
        var appDelegate: AppDelegate

	var body: some Scene {
		WindowGroup {
			ContentView(root:appDelegate.root)
		}
	}
}
class AppDelegate: NSObject, UIApplicationDelegate {
    let root: RootComponent = DefaultRootComponent(
        componentContext: DefaultComponentContext(lifecycle: ApplicationLifecycle())
    )
}