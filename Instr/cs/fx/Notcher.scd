Instr("cs.fx.Notcher.notcher", {
  arg bus, numChan, notchFreqs, notchrQs, notchdBs;

  var in, out, numNotches;

  in = In.ar(bus, numChan);
  out = in;

  numNotches = notchFreqs.size();

  for (0, numNotches - 1, {
    arg i;

    var notchFreq, notchrQ, notchdB;

    notchFreq = notchFreqs[i];
    notchrQ = notchrQs[i];
    notchdB = notchdBs[i];

    out = BPeakEQ.ar(out, notchFreq, notchrQ, notchdB);
  });

  out;
}, [
  \audiobus,
  ObjectSpec(1),
  \unipolar
]);
