DubBassEnvironment : ControllerEnvironment {
  var <>pat;

  init {
    var voicer,
      sock,
      dubBass = Instr("synths.DubBass"),
      dubBassSpecs = dubBass.specs;

    /*var api = API.new("DubBass"),
      me = this;*/

    /*var triggered_responder,
      me = this;*/

    super.init();

    voicer = MonoPortaVoicer(
      1,
      dubBass,
      [
        \amp, 0.5
      ]
    );
    voicer.mapGlobal(\amp);
    voicer.mapGlobal(
      \rateMultiplier,
      spec: dubBassSpecs.at(dubBass.argsAndIndices().at(\rateMultiplier))
    );

    /*MIDISyncClock.play({
      var tempo = MIDISyncClock.tempo();

      "tempo:".postln;
      tempo.postln;

      1;
    }, 1);*/

    voicer.portaTime = 0;

    sock = VoicerMIDISocket(
      [1, 0],
      voicer
    );

    sock.addControl(7, \amp);
    sock.addControl(15, \rateMultiplier);

    voicer.gui();

    /*"debug".postln;

    this.pat = Pmono(Instr("synths.DubBass").add.asDefName,
      \note,            Pseq([  0,    3,    7,    7,    0,    9,      3,    0     ], 1),
      \root,            4,
      \octave,          Pseq([  3,    3,    3,    3,    3,    2,      3,    2     ], 1),
      \rateMultiplier,  Pseq([  1/2,  6,    6,    6*2,  2,    8,      6,    6*2   ], 1),
    );

    "debug2".postln;
    
    api.add("trigger_loop", {
      arg loopName;

      "loopName1:".postln;
      loopName.postln;
      
      if((loopName != '-none-'), {

        "loopName2:".postln;
        loopName.postln;


        [>me.pat.play(MIDISyncClock, quant: 0);<]
        MIDISyncClock.play({
          "me:".postln;
          me.postln;
          [>me.pat.play();<]

          2;
        }, 2);
      
      });

      true;

    });

    [>api.mountOSC(addr: NetAddr("127.0.0.1", 6666));<]
    api.mountOSC();*/

    /*this.pat = Pmono(Instr("synths.DubBass").add.asDefName,
      \note,            Pseq([  0,    3,    7,    7,    0,    9,      3,    0     ], 1),
      \root,            4 + 12,
      \octave,          Pseq([  3,    3,    3,    3,    3,    2,      3,    2     ], 1),
      \rateMultiplier,  Pseq([  1/2,  6,    6,    6*2,  2,    8,      6,    6*2   ], 1),
      \tempo,           {MIDISyncClock.tempo();}
    );
    triggered_responder = OSCdef.new('DubBass/trigger_loop', {
      arg msg;

      var loopName = msg[1];

      if((loopName != '-none-'), {

        MIDISyncClock.sched(5, {
          
          me.pat.play(MIDISyncClock);

        });
      
      });

    }, '/DubBass/trigger_loop', recvPort: 6666);*/
  }
}
