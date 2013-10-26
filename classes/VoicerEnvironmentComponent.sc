VoicerEnvironmentComponent : PerformanceEnvironmentComponent {
  var <>voicer, <>sock, <>inChannel, <>outputChannel;

  init {
    arg params;
    var gui;
    
    super.init(params);

    this.inChannel = params['inChannel'];

    this.outputChannel = MixerChannel.new(
      nil,
      Server.default,
      2, 
      2,
      outbus: this.outputBus
    );

    if (params['numVoices'] == 1, {
      this.voicer = MonoPortaVoicer.new(
        1,
        params['instr'],
        target: this.outputChannel
      );
    }, {
      this.voicer = Voicer.new(
        params['numVoices'],
        params['instr'],
        target: this.outputChannel
      );
    });

    this.sock = VoicerMIDISocket.new(
      [
        MIDIClient.sources.indexOf(
          MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider")
        ),
        this.inChannel
      ],
      this.voicer
    );

    {this.init_done_callback.value();}.defer(1);
  }

  init_gui {

    arg params;
    var layout, voicerGui;
    
    super.init_gui(params);

    this.voicer.gui(params['layout']);

    //params['layout'].flow({
      //arg layout;

      //this.voicer.gui(layout);
    
    //});
      
    //voicerGui.resizeToFitContents();
    //voicerGui.background = Color.red;
    
    // resize manually for now.
    //voicerGui.layout.bounds = voicerGui.layout.bounds.resizeTo(
      //voicerGui.layout.bounds.width,
      //128.0
    //);

  
  }
}
