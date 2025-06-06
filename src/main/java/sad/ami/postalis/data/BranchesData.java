package sad.ami.postalis.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sad.ami.postalis.items.base.BranchType;

import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class BranchesData {
    private final Set<BranchType> branchTypes;

    @Builder.Default
    private final BranchType branchSelected = BranchType.NONE;

    public static final BranchesData DEFAULT = new BranchesData(Set.of(), BranchType.NONE);

    public static final Codec<BranchesData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BranchType.CODEC.listOf().xmap(Set::copyOf, List::copyOf).fieldOf("branches").forGetter(BranchesData::getBranchTypes),
                    BranchType.CODEC.fieldOf("selected").forGetter(BranchesData::getBranchSelected))
            .apply(instance, BranchesData::new));
}