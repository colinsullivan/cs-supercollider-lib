GenerativeSequencer : Object {

  var store,
    // the location of this sequencer's state in the state tree
    sequencerId,
    currentState,
    // if we are sending our sequence output to a SuperCollider synth, do so
    // through the [cruciallib Patch](https://github.com/crucialfelix/crucial-library)
    seqOutputPatch,
    // a patch needs an audio output channel
    patchOutputChannel,
    //TODO: MIDI out
    seqOutputMIDI,
    clock;
    // this is our EventGenerator subclass which will be handling the
    // generation of sequenced events to pass to the above outputs
    //seqGenerator;

  *new {
    arg params;

    ^super.new.init(params);
  }

  getStateSlice {
    //var stateAddressComponents,
      //stateSlice = store.getState();
    //// get slice of state tree
    //stateAddressComponents = sequencerId.split($.);
    //while({
      //(stateAddressComponents.size() > 0);
    //}, {
      //stateSlice = stateSlice[stateAddressComponents.removeAt(0).asSymbol()];
    //});
    //^stateSlice;
    var state = store.getState();
    ^state['sequencers'][sequencerId];
  }

  init {
    arg params;
    var me;

    store = params['store'];
    sequencerId = params['sequencerId'];

    currentState = this.getStateSlice();

    // create event generator
    //seqGenerator = currentState['seqGenerator']['class'].asSymbol().asClass()
      //.new(currentState);

    this.initOutputs();
    this.initSeqGenerator();


    // watch state store for updates
    me = this;
    store.subscribe({
      me.handleStateChange();
    });
  }
  
  create_output_channel {
    arg parentOutputChannel;
    ^MixerChannel.new(
      "GenerativeSequencer[" ++ currentState.name ++ "]" ,
      Server.default,
      2, 2,
      outbus: parentOutputChannel
    );
  }

  handleStateChange {
    var state = store.getState(),
      newState = this.getStateSlice(),
      abletonBeat,
      abletonBpm,
      tempo;

    abletonBeat = state.abletonlink.beat;
    abletonBpm = state.abletonlink.bpm;
    tempo = abletonBpm / 60.0;

    // advance clock
    if (clock == false, {
      clock = TempoClock.new(tempo: abletonBpm, beats: abletonBeat + (tempo * currentState['clockOffsetSeconds']));
    }, {
      clock.beats = abletonBeat + (tempo * currentState['clockOffsetSeconds'])
    });

    // if play is queued
    if (newState.playQueued == true, {
      this.queue(clock);
    });

    currentState = newState;

  }

  initSeqGenerator {

  }

  initOutputs {

  }

  queue {

  }

}
