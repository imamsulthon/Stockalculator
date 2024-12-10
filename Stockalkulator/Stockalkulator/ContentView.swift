import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()
    @State private var volume: Double = 50.0
    let name: String = MyEngine().someFunction()

	var body: some View {
        VStack(content: {
            Text("\(name)")
            Text(Greeting().greet())
            Button(action: /*@START_MENU_TOKEN@*/{}/*@END_MENU_TOKEN@*/, label: {
                Text("\(Int(volume))")
            })
            Slider(
                value: $volume,
                in: 0...100,
                step: 2,
                minimumValueLabel: Image(systemName: "speaker"),
                maximumValueLabel:Image(systemName: "speaker.wave.3"),
                label: {
                    Text("\(volume)")
                }
            ).padding(10)
        })
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
