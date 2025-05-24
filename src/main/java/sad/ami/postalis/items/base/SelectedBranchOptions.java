package sad.ami.postalis.items.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
public record SelectedBranchOptions(Set<BranchType> branchTypes) {
    public static final Codec<SelectedBranchOptions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(BranchType.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("branches").forGetter(SelectedBranchOptions::branchTypes))
                    .apply(instance, SelectedBranchOptions::new));
}
