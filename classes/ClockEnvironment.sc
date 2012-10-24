ClockEnvironment : Object {

  var <>interface, <>clockFace, <>midiClockOut;

  *new {
    ^super.new.init();
  }

  start {
    clockFace.play();
    /*this.midiClockOut.play();*/
  }

  stop {
    clockFace.stop();
    /*this.midiClockOut.stop();*/
  }

  request_tempo_update {
    var tempoRequest;
    tempoRequest = NetAddr.new("127.0.0.1", 6667);
    tempoRequest.sendMsg("/cs/to_ableton/request_tempo_update");
  }

  init {
    var window,
      toggleButton,
      clockFace,
      me = this,
      tempoResponder;

    /*MIDIClient.init;*/
    /*MIDIIn.connect(0, MIDIClient.sources[1]);*/
    MIDISyncClock.init();

    /*MIDISyncClock.sched(10, {
      Tempo.tempo = MIDISyncClock.tempo.round();
      10;
    });*/
    /*this.midiClockOut = MIDIClockOut.new("(in) To Ableton Tempo", "(in) To Ableton Tempo");*/

    tempoResponder = OSCdef.new('clockTempoResponder', {
      arg msg;

      var bpm = msg[1];

      Tempo.bpm = bpm;

    }, '/cs/from_ableton/tempo_update', recvPort: 6666);



    clockFace = ClockFace.new();
    this.clockFace = clockFace;
    window = clockFace.window();
    window.bounds.height = window.bounds.height + 50;

    toggleButton = Button(window, Rect(10, 80, 100, 30))
      .states_([
        ["start"],
        ["stop"]
      ])
      .action_({
        arg toggleButton;

        if (toggleButton.value == 1, {
          me.start();
        }, {
          me.stop();
        });

      });
  }

}
