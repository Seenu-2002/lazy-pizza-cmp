import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        InitKoinKt.doInitKoin { configuration in
        
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
