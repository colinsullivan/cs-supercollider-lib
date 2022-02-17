VoicerEnvironmentComponent : PerformanceEnvironmentComponent {
  var <>voicer, <>sock, <>inChannel, <>outputChannel;

  init {
    arg params;
    var gui, instrArgs = [], voiceBus, voiceTarget;
    
    if (params.includesKey('instr') == false, {
        "Error: instr parameter is required".throw();
    });

    if ((params.includesKey('numVoices') == false).and(params.includesKey('monoPortaVoicer') == false), {
      "Error: numVoices or monoPortaVoicer=true is required".throw();
    });

    super.init(params);

    this.inChannel = params['inChannel'];

    voiceBus = nil;
    voiceTarget = this.outputChannel;
    if (params.includesKey('voiceBus') == true, {
      voiceBus = params['voiceBus'];
      voiceTarget = nil;
    });


    if (params.includesKey('instrArgs'), {
      instrArgs = params['instrArgs'];
    });

    if (params['monoPortaVoicer'] == true, {
      this.voicer = MonoPortaVoicer.new(
        1,
        params['instr'],
        instrArgs,
        voiceBus,
        target: voiceTarget
      );
    }, {

      this.voicer = Voicer.new(
        params['numVoices'],
        params['instr'],
        instrArgs,
        voiceBus,
        target: voiceTarget
      );
    });

    this.sock = VoicerMIDISocket.new(
      [
        //MIDIClient.sources.indexOf(
          //MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider")
        //),
        //this.inChannel
        \all,
        this.inChannel
      ],
      this.voicer
    );

    this.sock.enable();

    if (params.includesKey('lowkeyName'), {
      this.sock.lowkey = params['lowkeyName'].notemidi();
    });

    if (params.includesKey('hikeyName'), {
      this.sock.hikey = params['hikeyName'].notemidi();
    });

    if (params.includesKey('transpose'), {
      this.sock.transpose = params['transpose'];
    });

    this.init_done_callback.value();
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
