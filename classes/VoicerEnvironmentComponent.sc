VoicerEnvironmentComponent : PerformanceEnvironmentComponent {
  var <>voicer, <>sock, <>inChannel;

  init {
    arg params;
    
    if (params.includesKey('instr') == false, {
        "Error: instr parameter is required".throw();
    });

    if ((params.includesKey('numVoices') == false).and(params.includesKey('monoPortaVoicer') == false), {
      "Error: numVoices or monoPortaVoicer=true is required".throw();
    });

    super.init(params);
  }

  init_patches {
    arg params;
    var gui, instrArgs = [], voiceBus, voiceTarget;

    this.inChannel = params['inChannel'];

    voiceBus = nil;
    if (params.includesKey('voiceBus') == true, {
      voiceBus = params['voiceBus'];
    }, {
      voiceBus = Bus.new('audio', params['outputBus']);
    });

    voiceTarget = nil;
    if (params.includesKey('voiceTarget') == true, {
      voiceTarget = params['voiceTarget'];
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
  }

  init_gui {
    arg params;
    super.init_gui(params);
    this.voicer.gui(params['layout']);
  }
}
