package sad.ami.postalis.items.base;

import lombok.Builder;

import java.util.Set;

@Builder(toBuilder = true)
public record SelectedBranchOptions(Set<BranchType> branchTypes) {
}
