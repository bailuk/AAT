#!/bin/bash


# Changes the namespace of the project. 
# Example: 
#  to change form ch.bailu.aat to ch.bailu.aat_test run:
#  bash scripts/change_namespace.bash aat aat_test 
# 
# Usage:
#  bash scripts/change_namespace.bash old new


[[ -d src/ch/bailu/$1 && $2 == [a-z]* ]] || { echo "Usage: $0 old_namespace new_namespace" >&2; exit 1; }


mv src/ch/bailu/$1 src/ch/bailu/$2 && \
   find -name "*.java" -exec \
      sh -c 'sed s/ch.bailu.'$1'./ch.bailu.'$2'./ {} > tmp && mv tmp {}' \; && \
   find -name "*.xml" -exec \
      sh -c 'sed s/ch.bailu.'$1'./ch.bailu.'$2'./ {} > tmp && mv tmp {}' \;


