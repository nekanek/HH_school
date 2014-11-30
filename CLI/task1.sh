#!/bin/sh
grep -LR 'import ru.hh.deathstar' --include '*.java' . > almost_harmless.txt