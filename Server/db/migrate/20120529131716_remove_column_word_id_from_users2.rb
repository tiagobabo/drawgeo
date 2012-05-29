class RemoveColumnWordIdFromUsers2 < ActiveRecord::Migration
  def up
  	remove_column :users, :word_id
  end

  def down
  end
end
